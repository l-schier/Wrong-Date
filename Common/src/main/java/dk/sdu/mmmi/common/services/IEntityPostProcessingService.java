package dk.sdu.mmmi.common.services;

import dk.sdu.mmmi.common.data.GameData;
import dk.sdu.mmmi.common.data.World;

public interface IEntityPostProcessingService {
    void process(GameData gameData, World world);
}
