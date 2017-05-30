package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;
import android.util.ArrayMap;

import com.couchbase.lite.Document;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CouchDocumentBaseTest extends DbBaseTestCase {
    private CouchDocumentBase with_empty;
    private CouchDocumentBase with_document;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        Document document = database.createDocument();
        // Due to a bug in Couchbase, documents will raise nullpointerexception if not saved...
        // So add some data and save to the database.
        Map<String, Object> content = new ArrayMap<>();
        content.put("key1", "value1");
        content.put("key2", 2);
        document.putProperties(content);

        with_empty = new CouchDocumentBase();
        with_document = new CouchDocumentBase(document);
    }

    @Test
    public void testCreateDocument() {
        assertNull(with_empty.document);
        with_empty.createDocument(database);
        assertNotNull(with_empty.document);

        String id = with_document.getId();
        with_document.createDocument(database);
        assertEquals(with_document.getId(), id);
    }

    @Test
    public void testGetId() {
        assertNull(with_empty.getId());
        assertNotNull(with_document.getId());
    }

    @Test
    public void testGetDatabase() {
        assertNull(with_empty.getDatabase());
        assertNotNull(with_document.getDatabase());
    }

    @Test
    public void testUpdate() {
        try {
            with_empty.update();
            Assert.fail();
        } catch (InstantiationError e) {
            // success
        }

        String revision_id = with_document.document.getCurrentRevisionId();
        with_document.update();
        assertNotSame(with_document.document.getCurrentRevisionId(), revision_id);
    }

    @Test
    public void testDelete() {
        try {
            with_empty.delete();
            Assert.fail();
        } catch (InstantiationError e) {
            //success
        }

        assertFalse(with_document.document.isDeleted());
        with_document.delete();
        assertTrue(with_document.document.isDeleted());
    }
}