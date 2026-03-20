class Intersection {
    private int intersectionID;
    private Building intersectionBuilding;
    private Player owner;

    public Intersection(int node) {
        intersectionID = node;
        this.intersectionBuilding = null;
        this.owner = null;
    }

    public int getIntersectionLocation() {
        return intersectionID;
    }    

    public void setBuilding(Building building) {
        intersectionBuilding = building;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public Building getBuilding() {
        return intersectionBuilding;
    }

    public Player getPlayer() {
        return owner;
    }
}