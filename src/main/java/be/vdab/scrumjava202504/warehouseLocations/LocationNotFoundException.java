package be.vdab.scrumjava202504.warehouseLocations;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String shelf, int position) {
        super("Location with shelf " + shelf + " and position " + position + " not found");
    }
}
