# SQLite Setup (Windows / PowerShell)

1. Download SQLite JDBC driver JAR (xerial sqlite-jdbc) and place it in:
   - lib/sqlite-jdbc.jar

2. Compile with JDBC JAR on classpath:
   - javac -cp ".;lib/*" -d bin src\*.java

3. Run app with JDBC JAR on classpath:
   - java -cp ".;bin;lib/*" CareerTracker

4. Database file auto-created in project root:
   - career_tracker.db

5. Auto-created tables:
   - students
   - skills

Notes:
- UI is unchanged; data now persists in SQLite.
- If JDBC driver is missing, app falls back to in-memory mode.
