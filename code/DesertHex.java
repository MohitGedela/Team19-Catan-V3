class DesertHex extends HexTerrain{

    public DesertHex(int hexID) {
        super(hexID, null);
    }

    @Override
    public ResourceType produceResource() {
        return null;
    }

    @Override
    public boolean productionStatus() {
        return false;
    }
}