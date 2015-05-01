package info.andersonpa.pocketleague;

import com.couchbase.lite.Context;
import com.couchbase.lite.NetworkReachabilityManager;
import com.couchbase.lite.android.AndroidSQLiteStorageEngineFactory;
import com.couchbase.lite.storage.SQLiteStorageEngineFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SandboxContext implements Context {
    private File rootDirectory;
    private File filesDir;

    public SandboxContext(String subdir, boolean deleteSubdirectory) {
        rootDirectory = new File(System.getProperty("user.dir"), "data/data/info.andersonpa.pocketleague/files");
        if (!rootDirectory.exists() && !rootDirectory.mkdir()) {
            throw new RuntimeException("Couldn't create temporary directory for listener!");
        }
        filesDir = new File(getRootDirectory(), subdir);

        if (deleteSubdirectory) {
            try {
                FileUtils.deleteDirectory(filesDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!filesDir.exists() && !filesDir.mkdir()) {
            throw new RuntimeException("Couldn't create directory " + filesDir.getAbsolutePath());
        }
    }

    public SandboxContext(String subdir) {
        this(subdir, true);
    }

    public SandboxContext() {
        this(true);
    }

    public SandboxContext(boolean deleteSubdirectory) {
        this("test", deleteSubdirectory);
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public SQLiteStorageEngineFactory getSQLiteStorageEngineFactory() {
        return new AndroidSQLiteStorageEngineFactory();
    }

    @Override
    public File getFilesDir() {
        return filesDir;
    }

    @Override
    public void setNetworkReachabilityManager(NetworkReachabilityManager networkReachabilityManager) {

    }

    @Override
    public NetworkReachabilityManager getNetworkReachabilityManager() {
        return new TestNetworkReachabilityManager();
    }

    class TestNetworkReachabilityManager extends NetworkReachabilityManager {
        @Override
        public void startListening() {

        }

        @Override
        public void stopListening() {

        }
    }
}
