package de.jcing.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import de.jcing.util.Clock;
import de.jcing.util.TimeTracker;
import lombok.Setter;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import de.jcing.Main;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.io.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window {

	private static final Logger LOG = LoggerFactory.getLogger(Window.class);
	private static final Logger GLFW_LOG = LoggerFactory.getLogger(GLFW.class);

	// The window handle
	private long window;

	private final TimeTracker tracker;

	private int width = 1280;
	private int height = 720;

	private boolean isResized;

	@Setter
	private boolean running;

	private int lastMillis = 0;
	private final ConcurrentLinkedDeque<Runnable> tasks;
	private final ConcurrentLinkedDeque<Runnable> loopTasks;
	private static final boolean VSYNC = false;

	public Window() {
		LOG.debug("Initializing Window using LWJGL " + Version.getVersion() + "!");
		tracker = new TimeTracker(60);
		tasks = new ConcurrentLinkedDeque<>();
		loopTasks = new ConcurrentLinkedDeque<>();
	}

	public void start() {
		running = true;
		try {
			init();
			loop();
		} catch(Throwable t){
			t.printStackTrace();
		} finally {
			end();
		}
	}

	private void init() {
		// Set up an error callback. The default implementation
		// will print the error message in System.err.
		GLFW.glfwSetErrorCallback((id, error) -> GLFW_LOG.error("GLFW {}: {}", id, error));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		// Required on macOS to get a core profile context:
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Set up a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(window, KeyBoard.keyCallBack);

		KeyBoard.addBinding(KeyBoard.ONPRESS, (key) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE)
				glfwSetWindowShouldClose(window, true);
			// We will detect this in the rendering loop
		});

		glfwSetMouseButtonCallback(window, Mouse.mouseButtonCallback);
		Mouse.addBinding(Mouse.ONPRESS, (_) -> LOG.debug("click @ (" + Mouse.getX() + "|" + Mouse.getY() + ")"));

		glfwSetFramebufferSizeCallback(window, (_, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResized(true);
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(VSYNC ? 1 : 0);

		// Make the window visible
		glfwShowWindow(window);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		// Set the clear color
		glClearColor(0.1f, 0.1f, 0.2f, 0.0f);
	}

	private void preLoop() {
		if (glfwWindowShouldClose(window)) {
			Main.finish();
		}

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// clear the framebuffer
	}
	
	private void runContext(ConcurrentLinkedDeque<Runnable> stuff) {
		for(Runnable r : stuff) {
			r.run();
		}
	}

	private void loop() {
		while(running) {
			runContext(tasks);
			tasks.clear();
			if (!tracker.shouldTick()) {
				continue;	
			}
			tracker.tick();
			preLoop();
			runContext(loopTasks);
			//LOG the framerate once per second!
			if (Clock.millis() - lastMillis > 1000) {
				lastMillis = Clock.millis();
				LOG.info(tracker.getCurrentTps() + " FPS!");
			}

			//update the Mouse
			GLFW.glfwGetCursorPos(window, Mouse.getXBuffer(), Mouse.getYBuffer());
			Mouse.update(width, height);
			postLoop();
		}
	}

	private void postLoop() {
		// swap the color buffers
		glfwSwapBuffers(window);

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	public void end() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void runInContext(Runnable r) {
		tasks.add(r);
	}
	
	public void loopInContext(Runnable r) {
		loopTasks.add(r);
	}

	public void setResized(boolean resized) {
		isResized = resized;
	}

	public boolean isResized() {
		return isResized;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
