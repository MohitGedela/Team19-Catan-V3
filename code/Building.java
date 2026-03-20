package code;

abstract class Building {

    public enum BuildingType {
        SETTLEMENT,
        CITY
    }

    private Intersection buildLocation;
    private Player buildOwner;

    public Building(Intersection buildingIntersection, Player owner) {
        buildLocation = buildingIntersection;
        buildOwner = owner;
    }

    public Intersection getBuildlocation() {
        return buildLocation;
    }

    public Player getOwner() {
        return buildOwner;
    }

    public abstract int getResourceMultiplier();

    public abstract BuildingType getBuildingType();

    public abstract Building upgrade(Player owner);

    public abstract int getVictoryPoints();
}