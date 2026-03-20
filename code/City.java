package code;

class City extends Building {

    public City(Intersection cityIntersection, Player owner) {
        super(cityIntersection, owner);
    }

    @Override
    public int getVictoryPoints() {
        return 2;
    }

    @Override
    public int getResourceMultiplier() {
        return 2;
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.CITY;
    }

    @Override
    public Building upgrade(Player owner) {
        throw new UnsupportedOperationException("Cannot upgrade a city");
    }
}