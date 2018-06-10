package Command;

import Main.CommitTree;
import Main.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Merge extends BaseCommand {

    private String branch;
    private Status masterStatus;
    private Status branchStatus;
    private Status branchedPoint;

    public Merge(String branch){
        this.branch = branch;
    }

    @Override
    public void execute() {
        CommitTree commitTree = CommitTree.getInstance();

        masterStatus = commitTree.getStatus("master");
        branchStatus = commitTree.getStatus(branch);
        branchedPoint = commitTree.getStatus("__" + branch);
        Status newStatus = Status.newInstance("master");

        HashMap<String, File> masterCheckList = new HashMap<>();
        HashMap<String, File> branchCheckList = new HashMap<>();
        HashMap<String, File> newList = new HashMap<>();

        fillCheckList(masterCheckList, masterStatus);
        fillCheckList(branchCheckList, branchStatus);

        Set<String> keys = branchCheckList.keySet();
        Iterator it = keys.iterator();

        while (it.hasNext()) {
            String key = (String)it.next();

            if (!masterCheckList.containsKey(key) && branchCheckList.containsKey(key)) {
                newList.put(key, branchCheckList.get("key"));
                newStatus.addFile(new File(branchCheckList.get("key").getAbsolutePath().replace(branchStatus.getBranchPath(),branchStatus.getRootPath())));
            } else if (masterCheckList.containsKey(key) && branchCheckList.containsKey(key)){
                // dialog 이용해 입력

                if(true){
                    // master branch에서 선택

                    branchCheckList.remove(key);
                }else{
                    // joining branch에서 선택
                }
            }
        }

        newList.putAll(branchCheckList);
        ArrayList<File> commitFileList =  newStatus.getCommittedFileList();

        keys = branchCheckList.keySet();
        it = keys.iterator();

        while (it.hasNext()) {
            String fName = (String)it.next();
            File f = branchCheckList.get(fName);

            File newFile = new File(newStatus.getBranchPath()+fName);
            copyFile(f, newFile);

            commitFileList.add(newFile);
        }

        newStatus.getAddedFileList().addAll(newStatus.getNewAddedFileList());
        commitTree.addCommitNode(newStatus);
        //Commit_DAO.Insert(status);
        Status.newInstance();
    }

    private void fillCheckList(HashMap<String, File> checkList_Map, Status status){
        CommitTree commitTree = CommitTree.getInstance();

        while (!branchedPoint.equals(status)) {
            ArrayList<File> commitedList = status.getCommittedFileList();

            Iterator it = commitedList.iterator();

            while (it.hasNext()) {
                File f = (File)it.next();
                String fName = f.getAbsolutePath().replace(status.getBranchPath(),"");

                if (!checkList_Map.containsKey(fName)) {
                    checkList_Map.put(fName, f);
                }
            }

            status = commitTree.getParent(status);
        }
    }

    private void copyFile(File from, File to){
        BufferedInputStream fis = null;
        BufferedOutputStream fos = null;

        try {
            if(!to.getParentFile().exists())
                to.getParentFile().mkdirs();

            fis = new BufferedInputStream(new FileInputStream(from));
            fos = new BufferedOutputStream(new FileOutputStream(to));

            byte[] buf = new byte[1024];
            int read;
            while((read = fis.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }

            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}