package info.andersonpa.pocketleague.db.tables;

import android.util.ArrayMap;

import com.couchbase.lite.Document;

import junit.framework.Assert;

import java.util.Map;

public class CouchDocumentBaseTest extends DbBaseTestCase {
    CouchDocumentBase with_empty;
    CouchDocumentBase with_document;

    protected void setUp() throws Exception {
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

    public void testCreateDocument() {
        assertNull(with_empty.document);
        with_empty.createDocument(database);
        assertNotNull(with_empty.document);

        String id = with_document.getId();
        with_document.createDocument(database);
        assertEquals(with_document.getId(), id);
    }

    public void testGetId() {
        assertNull(with_empty.getId());
        assertNotNull(with_document.getId());
    }

    public void testGetDatabase() {
        assertNull(with_empty.getDatabase());
        assertNotNull(with_document.getDatabase());
    }

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