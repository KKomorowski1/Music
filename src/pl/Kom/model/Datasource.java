package pl.Kom.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kacper on 15.12.2017.
 */
public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Kacper\\Projects\\Music\\" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;


    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;


    public static final String QUERY_ARTIST_FOR_SONG_START = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
            ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + "=\"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT = " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE";


    public static final String QUERY_ALBUMS_BY_ARTIST_START = "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON "
            + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + "=" + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " +
            TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + "=" + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME +
            ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + " WHERE " +
            COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = ?";

    public static final String INSERT_ARTSIST = "INSERT INTO " + TABLE_ARTISTS + '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS + '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";
    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS + '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";
    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";
    public static final String QUERY_SONGS = "SELECT " +COLUMN_SONG_ID+ " FROM "+TABLE_SONGS+ " WHERE "+ COLUMN_SONG_TITLE + " = ?";

    private Connection conn;

    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSong;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement querySongs;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTSIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSong = conn.prepareStatement(INSERT_SONG);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            querySongs = conn.prepareStatement(QUERY_SONGS);

            return true;
        } catch (SQLException e) {
            System.out.println("couldnt connect to database" + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (querySongInfoView != null) {
                querySongInfoView.close();
            }
            if (insertIntoArtists != null) {
                insertIntoArtists.close();
            }
            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }
            if (insertIntoSong != null) {
                insertIntoSong.close();
            }
            if (queryArtist != null) {
                queryArtist.close();
            }
            if (queryAlbum != null) {
                queryAlbum.close();
            }
            if (querySongs!=null){
                querySongs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("coudnt close connection: " + e.getMessage());

        }
    }

    public List<Artist> queryArtists(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) ;
            sb.append("DESC");
        } else {
            sb.append(" ASC ");
        }
        Statement statement = null;
        ResultSet results = null;
        try {
            statement = conn.createStatement();
            results = statement.executeQuery(sb.toString());

            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("query failed: " + e.getMessage());
            return null;
        } finally {

            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                System.out.println("error closing ResultSet" + e.getMessage());
            }

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("error closing Statment" + e.getMessage());
            }
        }
    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");


        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }

        }
        System.out.println("SQL statement = " + sb.toString());
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("query failed: " + e.getMessage());
            return null;
        }


    }

    public List<SongArtist> queryArtistsForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }
        System.out.println("SQL Statement: " + sb.toString());
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<SongArtist> songArtists = new ArrayList<>();
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void querySongMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Column %d in the songs table is names %s\n", i, meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Query failed : " + e.getMessage());
        }
    }

    public int getCount(String table) {
        String sql = "SELECT COUNT (*), MIN(_id) FROM " + table;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            int count = results.getInt(1);
            int min = results.getInt(2);
            System.out.format("Count = %d, Min = %d\n ", count, min);
            return count;
        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
            return -1;
        }
    }

    public boolean createViewForSongArtist() {
        try (Statement statement = conn.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException e) {
            System.out.println("Create view fail: " + e.getMessage());
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title) {
        try {
            querySongInfoView.setString(1, title);
            ResultSet results = querySongInfoView.executeQuery();
            List<SongArtist> songArtists = new ArrayList<>();
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private int insertArtist(String name) throws SQLException {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            //Insert the artist
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("couldnt insert artist!");
            }
            ResultSet generetedKeys = insertIntoArtists.getGeneratedKeys();
            if (generetedKeys.next()) {
                return generetedKeys.getInt(1);
            } else {
                throw new SQLException("couldnt get_id for artist");
            }
        }
    }

    private int insertAlbum(String name, int artistId) throws SQLException {
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            //Insert the album
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("couldnt insert album!");
            }
            ResultSet generetedKeys = insertIntoAlbums.getGeneratedKeys();
            if (generetedKeys.next()) {
                return generetedKeys.getInt(1);
            } else {
                throw new SQLException("couldnt get_id for album");
            }
        }
    }

    public void insertSong(String title, String artist, String album, int track) {

        try {

            conn.setAutoCommit(false);

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album, artistId);
            insertIntoSong.setInt(1, track);
            insertIntoSong.setString(2, title);
            insertIntoSong.setInt(3, albumId);

            int affectedRows = insertIntoSong.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Song insert failed");
            }


        } catch (Exception e) {
            System.out.println("Insert song exeception" + e.getMessage());
            try {
                System.out.println("performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("BAD" + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("reseting defalut commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("couldnt reset auto-commit" + e.getMessage());
            }
        }
    }
}


