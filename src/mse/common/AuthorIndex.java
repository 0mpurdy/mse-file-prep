package mse.common;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mj_pu_000 on 09/09/2015.
 */
public class AuthorIndex implements Serializable {

    private Author author;
    private HashMap<String, Integer> tokenCountMap;
    private HashMap<String, Integer> lastPage;
    private HashMap<String, Integer> nextReferenceIndex;
    private HashMap<String, String[]> references;

    public AuthorIndex(Author author) {
        this.author = author;
        tokenCountMap = new HashMap<>();
        lastPage = new HashMap<>();
        nextReferenceIndex = new HashMap<>();
        references = new HashMap<>();
    }

    public String getAuthorName() {
        return author.getName();
    }

    public HashMap<String, Integer> getTokenCountMap() {
        return tokenCountMap;
    }

    public void incrementTokenCount(String token, int volumeNumber, int pageNumber) {
        int count = -1;

        // if the token already exists
        if ((tokenCountMap.get(token)) != null) {

            // if the token should be ignored
            if (tokenCountMap.get(token) != -1) {
                count = tokenCountMap.get(token) + 1;

                // if the token is too frequent
                if (count < 10000) {
                    // if this page hasn't already been added
                    if (lastPage.get(token) != pageNumber) {

                        String[] currentReferenceList = references.get(token);
                        int index = nextReferenceIndex.get(token);
                        nextReferenceIndex.put(token, index + 1);

                        String currentReference = String.format("%d:%d", volumeNumber, pageNumber);

                        // if the next index is greater than the length of the array
                        if (index >= currentReferenceList.length) {
                            String[] newReferenceList = Arrays.copyOf(currentReferenceList, currentReferenceList.length * 10);
                            newReferenceList[index] = currentReference;
                            references.put(token, newReferenceList);
                        } else {
                            currentReferenceList[index] = currentReference;
                            references.put(token, currentReferenceList);
                        }
                    }
                } else {

                    // if the token is too frequent
                    // empty the maps and ignore any future tokens of this type
                    references.put(token, new String[0]);
                    nextReferenceIndex.put(token, -1);
                    lastPage.put(token, -1);

                    count = -1;
                }
            }
        } else {
            // if it is the first time this token has been found

            // create the new reference list
            String[] referencesList = new String[1];

            count = 1;

            // add the reference
            String currentReference = String.format("%d:%d", volumeNumber, pageNumber);
            referencesList[0] = currentReference;
            lastPage.put(token, 0);
            nextReferenceIndex.put(token, 1);
            references.put(token, referencesList);
        }
        tokenCountMap.put(token, count);
    }

    public int getTokenCount(String token) {
        if (tokenCountMap.get(token) != null) {
            return tokenCountMap.get(token);
        } else {
            return 0;
        }
    }

    public void cleanIndexArrays() {

        HashMap<String, String[]> newReferencesMap = new HashMap<>();

        for(Map.Entry<String, String[]> entry : references.entrySet()) {
            String token = entry.getKey();
            String[] oldReferences = entry.getValue();

            int nextReference = nextReferenceIndex.get(token);

            // ignore references with "nextReference" of -1
            if (nextReference != -1) {
                if (nextReference != oldReferences.length) {
                    String[] newReferences = Arrays.copyOf(oldReferences, nextReference);
                    newReferencesMap.put(token, newReferences);
                } else {
                    newReferencesMap.put(token, oldReferences);
                }
            }
        }
        references = newReferencesMap;
    }

    public Author getAuthor() {
        return author;
    }

    public String[] getReferences(String key) {
        return references.get(key);
    }

    public void loadIndex(String resLocation) {

        // try to load the index of the current author
        try {
            InputStream inStream = new FileInputStream(resLocation + author.getIndexFilePath());
            BufferedInputStream bInStream = new BufferedInputStream(inStream);
            ObjectInput input = new ObjectInputStream(bInStream);
            this.tokenCountMap = (HashMap<String, Integer>) input.readObject();
            this.references = (HashMap<String, String[]>) input.readObject();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Could not file find file: " + resLocation + author.getIndexFilePath());
        } catch (IOException ioe) {
            System.out.println("Error loading from: " + author.getIndexFilePath());
        } catch (ClassCastException cce) {
            System.out.println("Error casting class when loading new index");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class not found when loading new index");
        }

    }

    public void writeIndex(String location) {

        ObjectOutputStream objectOutputStream = null;

        try {

            File outputFile = new File(location);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            OutputStream file = new FileOutputStream(outputFile);
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            objectOutputStream = new ObjectOutputStream(buffer);
            objectOutputStream.writeObject(tokenCountMap);
            objectOutputStream.writeObject(references);
        }
        catch(IOException ex){
            System.out.println("\nError writing index for " + author.getName() + " at location " + location);
        } finally {
            if (!(objectOutputStream == null)) {
                try {
                    objectOutputStream.close();
                } catch (IOException ioe) {
                    System.out.println("Error closing: " + location);
                }
            }
        }
    }

}
