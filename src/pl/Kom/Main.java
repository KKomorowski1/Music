package pl.Kom;

import pl.Kom.model.Artist;
import pl.Kom.model.Datasource;
import pl.Kom.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if (!datasource.open()) {
            System.out.println("cant open datasoure");
            return;
        }
        List<Artist>artists = datasource.queryArtists(Datasource.ORDER_BY_NONE);
        if(artists==null){
            System.out.println("no Artists");
            return;
        }

        for (Artist artist : artists){
            System.out.println("ID = " + artist.getId() + ", "+ "Name = "+artist.getName()+", ");


        }

        List<String>albumsForArtist = datasource.queryAlbumsForArtist("Pink Floyd", datasource.ORDER_BY_DESC);
        for(String album : albumsForArtist){
            System.out.println(album);
        }

        List<SongArtist>songArtists = datasource.queryArtistsForSong("Go Your Own Way", Datasource.ORDER_BY_ASC);
        if(songArtists==null){
            System.out.println("couldnt find the artist for the song");
        }
        for (SongArtist artist:songArtists){
            System.out.println(" Artist name = "+artist.getArtistName()+", Album name = "+artist.getAlbumName()+", Track = "+ artist.getTrack());
        }
        datasource.querySongMetadata();

        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("number of songs = "+ count);

        datasource.createViewForSongArtist();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter song title : ");
        String title = sc.nextLine();
        songArtists = datasource.querySongInfoView(title);
        if(songArtists.isEmpty()){
            System.out.println("Coudnt find the artist for the song");
            return;
        }
        for (SongArtist artist :songArtists){
            System.out.println("FROM VIEW - Artist = "+artist.getArtistName()+" Album name = "+artist.getAlbumName()+" Track number = "+ artist.getTrack());
        }

        datasource.insertSong("Man's Not Hot", "Big Shaq", "Man's Not Hot", 1);
        datasource.close();
    }
}
