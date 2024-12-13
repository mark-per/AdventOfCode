package org.aoc.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day9 {
    private final String filePath;

    public Day9(String filePath) {
        this.filePath = filePath;
    }

    public long runPart(int part) throws IOException {
        String input = Files.readString(Paths.get(filePath)).trim();
        if (part == 1) {
            return solvePart1(input);
        } else if (part == 2) {
            return solvePart2(input);
        } else {
            throw new IllegalArgumentException("Invalid part: " + part);
        }
    }

    /*
     * PART 1:
     * Parse the input into a block array, where each file run is assigned an ID.
     * Then move all file blocks left and free spaces right as we did before.
     */
    private long solvePart1(String input) {
        Integer[] blocks = parseBlocks(input);
        defragPart1(blocks);
        return checksumBlocks(blocks);
    }

    /*
     * PART 2:
     * Use a sector representation. Each sector is either a file sector (id != null) or free sector (id == null).
     * Move files in descending ID order, attempting to place them into the leftmost free space that can hold them.
     */
    private long solvePart2(String input) {
        List<Sector> sectors = parseSectors(input);

        defragPart2(sectors);

        // Convert sectors back to a block array to compute checksum
        Integer[] blocks = sectorsToBlocks(sectors);
        return checksumBlocks(blocks);
    }

    /**
     * Parsing logic for Part 1:
     * Convert input to block-by-block array (like in part 1 logic).
     */
    private Integer[] parseBlocks(String input) {
        List<Integer> compressed = new ArrayList<>();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                compressed.add(c - '0');
            }
        }

        int size = 0;
        for (int x : compressed) size += x;

        Integer[] blocks = new Integer[size];
        int offset = 0;
        for (int i = 0; i < compressed.size(); i += 2) {
            int fileLen = compressed.get(i);
            int spaceLen = (i + 1 < compressed.size()) ? compressed.get(i + 1) : 0;

            for (int f = 0; f < fileLen; f++) {
                blocks[offset++] = i / 2;  // file ID is run index (i/2)
            }
            offset += spaceLen; // free space is null
        }

        return blocks;
    }

    /**
     * Parsing logic for Part 2:
     * Represent runs as sectors. Each pair of (fileLen, spaceLen) becomes up to two sectors:
     *  - A file sector if fileLen > 0
     *  - A free sector if spaceLen > 0
     */
    private List<Sector> parseSectors(String input) {
        List<Integer> compressed = new ArrayList<>();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) compressed.add(c - '0');
        }

        List<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < compressed.size(); i += 2) {
            int fileLen = compressed.get(i);
            int spaceLen = (i + 1 < compressed.size()) ? compressed.get(i + 1) : 0;

            if (fileLen > 0) {
                // file sector
                sectors.add(new Sector(i / 2, fileLen));
            }
            if (spaceLen > 0) {
                // free sector
                sectors.add(new Sector(null, spaceLen));
            }
        }

        return sectors;
    }

    /**
     * Part 1 defrag: move all file blocks left, free spaces right.
     */
    private void defragPart1(Integer[] blocks) {
        int left = 0;
        int right = blocks.length - 1;
        while (left < right) {
            while (left < right && blocks[left] != null) left++;
            while (left < right && blocks[right] == null) right--;
            if (left < right) {
                Integer temp = blocks[left];
                blocks[left] = blocks[right];
                blocks[right] = temp;
            }
        }
    }

    /**
     * Part 2 defrag:
     * Move entire files starting from the highest file ID to the lowest.
     * For each file:
     *  - Find the sector that holds it (it should be contiguous in one sector).
     *  - Find a free sector to the LEFT that can hold 'fileLen' blocks.
     *  - If the free sector is larger, split it.
     *  - Swap the file sector and the free sector.
     * If no suitable free sector is found, do not move the file.
     */
    private void defragPart2(List<Sector> sectors) {
        int maxFileId = findMaxFileId(sectors);
        for (int fileId = maxFileId; fileId >= 0; fileId--) {
            int fileIndex = findFileSectorIndex(sectors, fileId);
            if (fileIndex == -1) continue; // no such file

            Sector fileSec = sectors.get(fileIndex);
            int fileLen = fileSec.length;

            // Find suitable free sector to the LEFT
            int freeIndex = findFreeSectorToLeft(sectors, fileIndex, fileLen);
            if (freeIndex == -1) {
                // No suitable free sector
                continue;
            }

            Sector freeSec = sectors.get(freeIndex);

            // If freeSec is larger than needed, split it
            if (freeSec.length > fileLen) {
                // split freeSec
                Sector newSec = new Sector(null, freeSec.length - fileLen);
                freeSec.length = fileLen;
                sectors.add(freeIndex + 1, newSec);

                // Adjust indexes if needed
                if (fileIndex > freeIndex) {
                    fileIndex++; // because we inserted a new sector before file?
                }
                // Actually, since we found a space to the left, and we only insert after freeIndex,
                // if fileIndex > freeIndex, it shifts by one.
                // We'll handle carefully by re-fetching references after insertion.
                freeSec = sectors.get(freeIndex); // re-fetch after potential modification
                fileSec = sectors.get(fileIndex);
            }

            // Now we have freeSec exactly the same size as fileLen
            // Move file by swapping sectors
            // According to puzzle instructions, we want to place the file in the free space found.
            // This means we effectively should put fileSec where freeSec is, and freeSec where fileSec was.
            // Since we must preserve order, we can just swap them.
            Sector temp = fileSec;
            sectors.set(fileIndex, freeSec);
            sectors.set(freeIndex, temp);
        }
    }

    private int findMaxFileId(List<Sector> sectors) {
        int max = -1;
        for (Sector s : sectors) {
            if (s.id != null && s.id > max) max = s.id;
        }
        return max;
    }

    private int findFileSectorIndex(List<Sector> sectors, int fileId) {
        for (int i = 0; i < sectors.size(); i++) {
            Sector s = sectors.get(i);
            if (s.id != null && s.id == fileId) return i;
        }
        return -1;
    }

    /**
     * Find a free sector to the LEFT of fileIndex that can hold 'fileLen' blocks.
     * We search from the leftmost sector up to fileIndex-1.
     * The puzzle says "to the left" meaning anywhere before the file.
     * We pick the LEFTMOST sector that can fit the file.
     */
    private int findFreeSectorToLeft(List<Sector> sectors, int fileIndex, int fileLen) {
        int bestIndex = -1;
        for (int i = 0; i < fileIndex; i++) {
            Sector s = sectors.get(i);
            if (s.id == null && s.length >= fileLen) {
                bestIndex = i;
                break; // leftmost suitable sector
            }
        }
        return bestIndex;
    }

    /**
     * Convert sectors back to a block array for checksum calculation.
     */
    private Integer[] sectorsToBlocks(List<Sector> sectors) {
        int totalLen = 0;
        for (Sector s : sectors) totalLen += s.length;
        Integer[] blocks = new Integer[totalLen];

        int offset = 0;
        for (Sector s : sectors) {
            if (s.id == null) {
                // free space
                for (int i = 0; i < s.length; i++) {
                    blocks[offset++] = null;
                }
            } else {
                // file
                for (int i = 0; i < s.length; i++) {
                    blocks[offset++] = s.id;
                }
            }
        }

        return blocks;
    }

    private long checksumBlocks(Integer[] blocks) {
        long sum = 0;
        for (int i = 0; i < blocks.length; i++) {
            Integer fileId = blocks[i];
            if (fileId != null) {
                sum += (long) i * fileId;
            }
        }
        return sum;
    }

    // Simple class to represent sectors
    static class Sector {
        Integer id; // null for free space, non-null for file
        int length;

        Sector(Integer id, int length) {
            this.id = id;
            this.length = length;
        }
    }
}
