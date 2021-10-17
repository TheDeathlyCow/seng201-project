package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

/**
 * Stores really long methods related to the view ship button.
 */
public class ViewShipGUI {

    /**
     * Returns an HTML formatted description of a ship.
     *
     * @param ship Ship to get the description of.
     * @return Returns an HTML formatted string of the ship, or an empty string
     * if the ship is null.
     */
    public static String getHTMLDescription(PlayerShip ship) {
        if (ship == null)
            return "";

        return "<html>" +
                "<style>" +
                ".linebreak-div p {" +
                "   margin-bottom: 7px;" +
                "}" +
                "<div id='attributes' class='linebreak-div'>" +
                "<h2>" + ship.getName() + "</h2>" +
                "<p>Max Health: " + ship.getAttributes().getMaxHealth() + "</p>" +
                "<p>Speed: " + (int) ship.getAttributes().getSpeed() + " km / day </p>" +
                "<p>Damage: " + ship.getAttributes().getDamage() + "</p>" +
                "<p>Armour: " + ship.getAttributes().getArmour() + "</p>" +
                "<p>Number of Beds: " + ship.getAttributes().getNumBeds() + "</p>" +
                "<p>Maximum Weight: " + ship.getAttributes().getMaxWeight() + " Mg </p>" +
                "<p>Base Weight: " + ship.getAttributes().getBaseWeight() + " Mg </p>" +
                "<p>Cargo Slots: " + ship.getCargoManager().getMaxSize() + "</p>" +
                "<p>Upgrade Slots: " + ship.getUpgrades().getMaxSize() + "</p>" +
                "</div>" +
                "</html>";

    }

    /**
     * Returns an HTML formatted status string of a ship.
     *
     * @param ship Ship to get the status of.
     * @return Returns an HTML formatted status string of a ship.
     */
    public static String getStatusString(PlayerShip ship) {
        String status = "<html><style>.list { margin: 5px; }</style>" +
                "<div class='list'><dl>" +
                "<dt>- Name: " + ship.getName() + "</dt>" +
                String.format("<dt>- Current health: %d / %d (%.0f%%)</dt>",
                        ship.getHealth(),
                        ship.getAttributes().getMaxHealth(),
                        ship.getHealthAsPercentage()) +
                "<dt>- Crew: " + ship.getNumCrew() + "</li>" +
                "<dt>- Number of Beds: " + ship.getAttributes().getNumBeds() + "</dt>" +
                "<dt>- Minimum crew to sail: " + ship.getMinCrewToSail() + "</dt>" +
                String.format("<dt>- Wages per day: %.2f Doubloons</dt>", ship.getCrewWagesPerDay()) +
                String.format("<dt>- Speed: %d km / day</dt>", (int) ship.getAttributes().getSpeed()) +
                "<dt>- Armour: " + ship.getAttributes().getArmour() + "</dt>" +
                "<dt>- Damage: " + ship.getAttributes().getDamage() + "</dt>" +
                "<dt>- Current weight: " + ship.getCurrentWeight() + "</dt" +
                "<dt>- Max weight: " + ship.getAttributes().getMaxWeight() + "</dt>" +
                String.format("<dt>- Cargo: %d / %d slots filled</dt>",
                        ship.getCargoManager().size(),
                        ship.getCargoManager().getMaxSize()) +
                String.format("<dt>- Upgrades (%d / %d slots filled)</dt>",
                        ship.getUpgrades().size(),
                        ship.getUpgrades().getMaxSize());
        StringBuilder upgrades = new StringBuilder();
        ship.getUpgrades()
                .forEach(u -> upgrades.append("<dd>- ").append(u.toString()).append("</dd>"));

        return status + upgrades.toString() + "</dl></div></html>";

    }
}
