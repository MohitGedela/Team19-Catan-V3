package code;

class ResourceHex extends HexTerrain {

    private ResourceType resource;

    public ResourceHex(int hexID, HexBoardNum hexNumber, ResourceType resource) {
        super(hexID, hexNumber);
        this.resource = resource;
    }

    @Override
    public ResourceType produceResource() {
        return resource;
    }

    @Override
    public boolean productionStatus() {
        return true;
    }
}