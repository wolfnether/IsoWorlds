package common;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import sponge.util.Logger;
import sponge.util.Utils;

import java.util.*;

public class WorldManager {
    private Map<String, Integer> countdown;

    public WorldManager(){
        this.countdown = new HashMap<>();
    }

    public void isoworldLoaded(World world) {
        countdown.put(world.getName(), 0);
    }

    public boolean loadIsoworld(World world){
        isoworldLoaded(world);
        return true;
    }

    public boolean unloadIsoworld(World world){
        try {
            world.save();
            if (Utils.isMirrored(world)) {
                this.fixMirror(world);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        countdown.remove(world.getName());
        return true;
    }

    private void fixMirror(World world) {
        sponge.util.Logger.severe("--- Anomalie détectée, unload interrompu et suppression de l'anomalie: " + world.getName() + " ---");
        Sponge.getServer().unloadWorld(world);
        Sponge.getServer().deleteWorld(world.getProperties());
        Logger.severe("--- Anomalie: Corrigée, suppression effectuée avec succès de l'isoworld: " + world.getName() + " ---");
    }

    public void resetCountdown(World world){
        countdown.put(world.getName(), 0);
    }

    public int incrementCountdown(World world){
        int countdown = this.countdown.get(world.getName());
        countdown += 1;
        this.countdown.put(world.getName(), countdown);
        return countdown;
    }

    public ArrayList<World> getIsoworld(){
        ArrayList<World> isoWorld = new ArrayList<>();
        for (World world : Sponge.getServer().getWorlds()) {
            if(world.getName().endsWith("-IsoWorld")){
                isoWorld.add(world);
            }
        }
        return isoWorld;
    }

    public ArrayList<World> getLoadedIsoworld(){
        ArrayList<World> isoworld = getIsoworld();
        isoworld.removeIf(Extent::isLoaded);
        return isoworld;
    }

    public ArrayList<World> getUnloadedIsoworld(){
        ArrayList<World> isoworld = getIsoworld();
        isoworld.removeIf(world -> !world.isLoaded());
        return isoworld;
    }
}
