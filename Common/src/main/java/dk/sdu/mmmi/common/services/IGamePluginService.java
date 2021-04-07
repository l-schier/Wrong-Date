package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;

public interface IGamePluginService {
    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}
