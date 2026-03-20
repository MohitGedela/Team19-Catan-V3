class Settlement extends Building {

    public Settlement(Intersection settlementIntersection, Player owner) {
        super(settlementIntersection, owner);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }

    @Override
    public int getResourceMultiplier() {
        return 1;
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.SETTLEMENT;
    }

    @Override
    public Building upgrade(Player owner) {
        return new City(getBuildlocation(), owner);
    }
}