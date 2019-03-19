package cs445.a1;
import java.lang.*;

public class Profile implements ProfileInterface {
    private Set<ProfileInterface> following;
    private String name;
    private String about;
    
    
    public Profile() {
        name = "";
        about = "";
        following = new Set<>();
    }
    
    public Profile(String name, String about) {
        this.name = name;
        this.about = about;
        if (name == null)
            setName("");
        if (about == null)
            setAbout("");    
        following = new Set<>();
    }
    
    @Override
    public void setName(String newName) throws IllegalArgumentException {
        if (newName == null)
            throw new java.lang.IllegalArgumentException();
        else
            name = newName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setAbout(String newAbout) throws IllegalArgumentException {
        if (newAbout == null)
            throw new java.lang.IllegalArgumentException();
        else
            about = newAbout;
    }

    @Override
    public String getAbout() {
        return about;
    }

    @Override
    public boolean follow(ProfileInterface other) {
        boolean toReturn = false;
        if (other!=null) {
            try {
                toReturn = following.add(other);
            } catch (SetFullException e) {
                return toReturn;
            } 
         }
        return toReturn;
    }

    @Override
    public boolean unfollow(ProfileInterface other) {
        if (other==null || !following.contains(other))
            return false;
        else
            return following.remove(other);
    }
    
    @Override
    public ProfileInterface[] following(int howMany) {
        int size = howMany;
        if (following.getCurrentSize() <= howMany)
            size = following.getCurrentSize();
        
        ProfileInterface[] result = new ProfileInterface[size];
        Object[] copy= following.toArray();
        for (int i = 0; i< size; i++) {    
            result[i] = (ProfileInterface)copy[i];
        }
       return result;
    }

    @Override
    public ProfileInterface recommend() {
        ProfileInterface recommend = null, follower1 = null;
        ProfileInterface[] followingProfiles1 = this.following(1000);
        ProfileInterface[] followingProfiles2 = null;
        
        for (int i = 0; i<following.getCurrentSize(); i++){
            follower1 = followingProfiles1[i];
            if (follower1!= this && follower1!=null){
                followingProfiles2 = follower1.following(1000);
                for (int j = 0; j<1000; j++){
                    try {
                        if (followingProfiles2[j]!=this && followingProfiles2[j]!=null){
                        recommend = followingProfiles2[j];
                        break;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return recommend;
                    }
                }       
            }
        }  
        return recommend;
    }
    //if no response from garrison. throw an exception.....
}
