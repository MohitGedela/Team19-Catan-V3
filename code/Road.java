class Road {
    private Player owner;
    private Edge location;

    public Road (Player owner, Edge location) {
        this.owner = owner;
        this.location = location;
    }

    public Edge getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }
}