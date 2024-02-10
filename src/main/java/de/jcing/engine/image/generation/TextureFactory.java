package de.jcing.engine.image.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcing.engine.image.Animation;
import de.jcing.engine.image.ImageData;
import de.jcing.engine.image.TextureAtlas;
import de.jcing.util.Maths;
import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;

public class TextureFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TextureFactory.class);
    private final ArrayList<Instruction> instructions;

    public TextureFactory() {
        instructions = new ArrayList<>();
    }

    public TextureFactory addJSONInstructions(String JSONFile) {
        try {
            String location = new File(JSONFile).getParent().replace("\\", "/") + "/";
            @Cleanup
            InputStream is = TextureFactory.class.getResourceAsStream(JSONFile);
            @Cleanup
            InputStreamReader isr = new InputStreamReader(is);
            @Cleanup
            BufferedReader br = new BufferedReader(isr);
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONObject o = new JSONObject(jsonContent.toString());

            JSONObject data = o.getJSONObject("data");
            for (String key : data.keySet()) {
                JSONObject img = data.getJSONObject(key);
                String name = key;
                String path = new StringBuilder(location).append(img.getString("path")).toString();
                String ext = img.has("ext") ? img.getString("ext") : null;
                int count = img.has("count") ? img.getInt("count") : 1;
                int rows = img.has("rows") ? img.getInt("rows") : 1;
                int cols = img.has("cols") ? img.getInt("cols") : 1;
                Instruction.Type type = img.has("type") ? switch (img.getString("type")) {
                    case "multi" -> Instruction.Type.multi;
                    case "single" -> Instruction.Type.single;
                    default -> Instruction.Type.single;
                } : Instruction.Type.single;
                instructions.add(new Instruction(type, name, path, ext, cols, rows, count));
            }
        } catch (IOException e) {
            LOG.error("Could not parse Texture JSON desciptor! }\n{}", e);
        }
        return this;
    }

    public TextureBuilder build() throws IOException {
        return new TextureBuilder(instructions);
    }

    @Data
    public static class Instruction {
        public static enum Type {
            single, multi
        }

        private final Type type;
        private final String name;
        private final String path;
        private final String ext;
        private final int cols;
        private final int rows;
        private final int count;
    }

    @Data
    public static class ImageGroup {
        private final String name;
        private final int startIndex;
        private final int count;
    }

    public static class TextureBuilder {
        public static final String[] IMAGE_EXTENSIONS = { ".png" };

        @Getter
        private TextureAtlas atlas;
        private final HashMap<String, Animation> animations;
        private final ArrayList<ImageGroup> groups;

        private final ArrayList<ImageData> datas;

        private TextureBuilder(List<Instruction> instructions) throws IOException {
            animations = new HashMap<>();
            datas = new ArrayList<>();
            groups = new ArrayList<>();
            for (Instruction i : instructions) {
                ImageData[] data = switch (i.type) {
                    case single -> loadSingle(i.path, i.count, i.ext);
                    case multi -> loadMulti(i.path, i.rows, i.cols, i.count, i.ext);
                };
                groups.add(new ImageGroup(i.name, datas.size(), i.count));
                datas.addAll(Arrays.asList(data));
            }
            atlas = mergeAtlas(datas);
            for (ImageGroup g : groups) {
                LOG.debug("found animation '{}' with {} frames.", g.name, g.count);
                animations.put(g.name, new Animation(atlas, g.startIndex, g.count));
            }
        }

        private ImageData[] loadMulti(String path, int rows, int cols, int count, String ext) throws IOException {
            String fullPath = path.endsWith(ext) ? path : new StringBuilder(path).append(".").append(ext).toString();
            ImageData data = ImageData.from(fullPath);
            ImageData[] splitted = data.split(rows, cols, count);
            return splitted;
        }

        private ImageData[] loadSingle(String path, int count, String ext) throws IOException {
            ImageData[] data = new ImageData[count];
            for (int i = 0; i < count; i++) {
                data[i] = ImageData.from(new StringBuilder(path).append(i).append(".").append(ext).toString());
            }
            return data;
        }

        public Animation getAnimation(String key) {
            return animations.get(key);
        }

        private List<File> loadFullDirectory(String directoryPath) {
            File dir = new File(directoryPath);
            LOG.info("Loading animation from: {}", dir.getAbsolutePath());
            File[] expanded = dir.listFiles();
            ArrayList<File> imageFiles = new ArrayList<>();
            if (expanded != null) {
                for (File f : expanded) {
                    if (isValidImagePath(f.getName())) {
                        imageFiles.add(f);
                    }
                }
            }
            return imageFiles;
        }

        private TextureAtlas mergeAtlas(Collection<ImageData> images) throws IOException {
            int count = images.size();

            int texturesPerSide = Maths.roundUp(Math.sqrt(count));
            int size = images.iterator().next().getWidth() * texturesPerSide;
            LOG.debug("combining {} images to atlas with {} textures per side and a size of {}px", count,
                    texturesPerSide, size);

            ImageData combined = new ImageData(size, size);
            int x = 0;
            int y = 0;
            for (ImageData subImage : images) {
                combined.writeTo(subImage, x, y);
                x += subImage.getWidth();
                if (x >= size) {
                    x = 0;
                    y += subImage.getWidth();
                }
            }
            TextureAtlas atlas = new TextureAtlas(combined, texturesPerSide, texturesPerSide, count);
            return atlas;
        }

        // Helper Methods
        private boolean isValidImagePath(String path) {
            for (String s : IMAGE_EXTENSIONS) {
                if (path.endsWith(s))
                    return true;
            }
            return false;
        }
    }
}
