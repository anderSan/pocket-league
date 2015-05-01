package info.andersonpa.gametemplate.db;

public class DatabaseUpgrader {
    public static String replaceNulls(String tableName, String columnName, String value) {
        return "UPDATE " + tableName + " SET " + columnName + "=" + value + " where " +
                columnName + " is NULL;";

    }

    public static String addColumn(String tableName, String columnName, String type,
                                   String defaultValue) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + " DEFAULT " +
                "" + defaultValue + ";";
    }

    public static String addBooleanDefaultZeroColumn(String tableName, String columnName) {
        return addColumn(tableName, columnName, "BOOLEAN", "0");
    }

    public static boolean are_scores_equal(int[] oldScores, int[] newScores) {
        return oldScores[0] == newScores[0] && oldScores[1] == newScores[1];
    }
}
