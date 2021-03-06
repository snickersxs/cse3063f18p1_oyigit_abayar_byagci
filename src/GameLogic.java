import java.util.*;

public class GameLogic {

    private List<Player> players = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    public void startGame() {
        Scanner scn = new Scanner(System.in);
        Scanner scnline = new Scanner(System.in);
        Scanner scnUnused = new Scanner(System.in);
        int totalPlayerNum;

        do{
            System.out.println("Enter the number of players you want :");
            totalPlayerNum=scn.nextInt();
            if (totalPlayerNum<2 || totalPlayerNum>4){
                System.out.println("Error: Player number must between 2..4");
            }
        } while (totalPlayerNum<2 || totalPlayerNum>4);


        int i = 1;
        do {
            System.out.println("Enter Player"+i +" name: " );
            String name = scnline.nextLine();
            if (hasThisName(players,name)) {
                System.out.println("You can't take this name.");
                continue;
            }
            players.add(new Player(name));
            i++;
        } while (i <= totalPlayerNum);

        // Create locations and add them to the locations arraylist
        locations.add(new Location("START", 0));
        locations.add(new LocationCity("OLD KENT ROAD", 1,
                60, 10, null));

        locations.add(new LocationLuckyCard("CHANCE",2));

        locations.add(new LocationCity("WHITECHAPEL", 3,
                60, 10, null));

        locations.add(new LocationTaxAdmin("INCOME TAX", 4, 200)); // TAX ADMINISTRATION LOCATION

        locations.add(new LocationCity("KINGS CROSS ", 5,
                200, 40, null));

        locations.add(new LocationCity("THE ANGEL ISLINGTON", 6,
                100, 20, null));

        locations.add(new LocationLuckyCard("CHANCE",7));

        locations.add(new LocationCity("EUSTON ROAD", 8,
                100, 20, null));
        locations.add(new LocationCity("PENTONVILLE ROAD", 9,
                120, 25, null));
        locations.add(new LocationJail("JAIL", 10, 0));// JAIL LOCATION

        locations.add(new LocationCity("PALL MALL", 11,
                140, 30, null));
        locations.add(new LocationTaxAdmin("ELECTRIC COMPANY", 12, 150)); // TAX ADMINISTRATION LOCATION

        locations.add(new LocationCity("WHITEHALL", 13,
                140, 30, null));
        locations.add(new LocationCity("NORTHUMBERLAND AVENUE", 14,
                160, 35, null));
        locations.add(new LocationCity("MARLEYBONE STATION", 15,
                200, 40, null));
        locations.add(new LocationCity("BOW STREET", 16,
                180, 35, null));
        locations.add(new LocationLuckyCard("CHANCE",17));

        locations.add(new LocationCity("MARLBROROUGH STREET", 18,
                180, 35, null));
        locations.add(new LocationCity("VINE STREET", 19,
                200, 40, null));
        locations.add(new LocationTaxAdmin("FREE PARKING", 20, 0)); // BURADA HICBISEY OLMAMASI DOGRU

        locations.add(new LocationCity("STRANT", 21,
                220, 45, null));
        locations.add(new LocationLuckyCard("CHANCE",22));

        locations.add(new LocationCity("FLEET STREET", 23,
                220, 45, null));
        locations.add(new LocationCity("TRAFALGAR SQUARE", 24,
                240, 50, null));
        locations.add(new LocationCity("FENCHURCH STREET STATION", 25,
                200, 40, null));
        locations.add(new LocationCity("LEICESTER SQUARE", 26,
                260, 55, null));
        locations.add(new LocationCity("COVENTRY STREET", 27,
                260, 55, null));
        locations.add(new LocationTaxAdmin("WATER WORKS", 28, 150));
        locations.add(new LocationCity("PICCADILLY", 29,
                280, 60, null));


        locations.add(new LocationJail("GO TO JAIL", 30, 1));


        locations.add(new LocationCity("REGENT STREET", 31,
                300, 65, null));
        locations.add(new LocationCity("OXFORD STREET", 32,
                300, 65, null));

        locations.add(new LocationLuckyCard("CHANCE",33));

        locations.add(new LocationCity("BOND STREET", 34,
                320, 70, null));

        locations.add(new LocationCity("LIVERPOOL STREET STATION", 35,
                200, 40, null));

        locations.add(new LocationLuckyCard("CHANCE",36));

        locations.add(new LocationCity("PARKLANE", 37,
                350, 75, null));
        locations.add(new LocationTaxAdmin("SUPER TAX", 38, 100));
        locations.add(new LocationCity("MAYFAIR", 39,
                400, 80, null));



        while(isGameContinue(players)) {
            for (Player player : players) {
                System.out.println("Player "+player.getName()+"'s turn");
                if(player.isInJail()) {
                    System.out.println(player.getName()+" is in jail.");
                    player.setInJail(false); // for exiting from jail on the next round
                    continue;
                }
                System.out.println("Press any key to roll dice");
                scnUnused.nextLine();
                // player rolls dice
                Dice dice1 = new Dice();
                Dice dice2 = new Dice();
                dice1.generateRandomValFromDice();
                dice2.generateRandomValFromDice();

                int diceResult = Dice.getDiceResult(); // get static dice result
                Dice.setDiceResult(0);
                System.out.println("Dice result: " + diceResult);
                int playerNewLocationIndex = player.getCurrLocationIndex() + diceResult;

                if (playerNewLocationIndex >= locations.size()){
                    player.setCash(player.getCash()+ 100);
                    playerNewLocationIndex = playerNewLocationIndex % locations.size();
                }
                player.setCurrLocationIndex(playerNewLocationIndex);
                int playerLocIndexAfterMove = player.getCurrLocationIndex();
                Location playerLocAfterMove = locations.get(playerLocIndexAfterMove);
                System.out.println("You are now on " + playerLocAfterMove.getName().toUpperCase());

                if (playerLocAfterMove instanceof LocationJail) {
                    LocationJail playerLocAfterMove1 = (LocationJail) playerLocAfterMove;
                    if (playerLocAfterMove1.getType() != 0) { // it means this block is goToJail so block the player and take him to the jail
                        player.setInJail(true);
                        player.setCurrLocationIndex(LocationJail.jailLocationIndex);
                    }
                } else if (playerLocAfterMove instanceof LocationTaxAdmin) {
                    LocationTaxAdmin playerLocAfterMove1 = (LocationTaxAdmin) playerLocAfterMove;
                    player.setCash(player.getCash()-playerLocAfterMove1.getTaxPrice());
                    System.out.println(player.getName()+" paid " + playerLocAfterMove1.getTaxPrice()
                            +"$ You now have "+ player.getCash());
                } else if (playerLocAfterMove instanceof LocationLuckyCard) {
                    LocationLuckyCard playerLocAfterMove1 = (LocationLuckyCard) playerLocAfterMove;
                    playerLocAfterMove1.imFeelingLucky(player,locations);
                } else if (playerLocAfterMove instanceof LocationCity){
                    LocationCity playerLocAfterMove1 = (LocationCity) playerLocAfterMove;
                    if (playerLocAfterMove1.isLocationOwned()) {
                        int rentAmount = playerLocAfterMove1.getRentPrice(); // calculate rent price
                        player.setCash(player.getCash() - rentAmount); // decrease leaseholder player's cash
                        playerLocAfterMove1.getOwner().setCash(playerLocAfterMove1.getOwner().getCash() + rentAmount); // increase owner player's cash
                    } else {
                        Scanner scn2 = new Scanner(System.in);
                        System.out.println("You have: " + player.getCash() + "$ and "
                                + playerLocAfterMove1.getName().toUpperCase()
                                + "'s price: "+ playerLocAfterMove1.getPrice() + "$");
                        System.out.println("Do you want to buy this location? (Y/n):");
                        String userChoice = scn2.nextLine();
                        if (userChoice.equals("Y") || userChoice.equals("y")) {
                            int price = playerLocAfterMove1.getPrice();
                            if (player.getCash() < price){
                                System.out.println("Sorry, You don't have enough money.");
                            } else {
                                player.setCash(player.getCash() - price);
                                playerLocAfterMove1.setOwner(player);
                                player.getOwnedLocations().add(playerLocAfterMove1);
                                System.out.println(player.getName()+" bought location:"+playerLocAfterMove1.getName()+" and remaining money is:"+player.getCash());
                            }

                        }
                    }
                }
                System.out.println("\n**********************\n");
            }
        }


    }


    public boolean isGameContinue(List<Player> players) {
        Iterator<Player> iterPlayers = players.iterator();
        while (iterPlayers.hasNext()) {
            Player player = iterPlayers.next();
            if (player.getCash() < 0) {
                if (player.getOwnedLocations().size() > 0) {
                    System.out.println("Player: " + player.getName() +
                            " has less than 0$ so hi/she is in debt so his/her owned lcoations will be sold/foreclosed(haciz)");
                    List<LocationCity> playersOwnedLocations = player.getOwnedLocations(); // get player's owned locations
                    Collections.sort(playersOwnedLocations);
                    Iterator<LocationCity> iterOwnedCities = playersOwnedLocations.iterator();
                    while (iterOwnedCities.hasNext()) {
                        LocationCity city = iterOwnedCities.next();
                        int price = city.getPrice();
                        player.setCash(player.getCash() + price);
                        System.out.println("Player " + player.getName() + "'s owned location " +
                                city.getName() + " has been sold/foreclosed now and so Player " +
                                player.getName() + " now have " + player.getCash() + "$");
                        city.setOwner(null);
                        iterOwnedCities.remove();
                        if (player.getCash() >= 0) { // if his/her cash is more than 0, dont sell owned locations anymore so break it
                            break;
                        }
                    }
                }
                if (player.getCash() < 0) { // if player's cash is still less than 0, player is bankrupted so it will be eliminated, eliminate it
                    System.out.println("Player " + player.getName() + " is in debt and has no owned city to be foreclosed so He/She is bankrupted so eliminated");
                    iterPlayers.remove();
                }
            }
        }

        if(players.size() == 1) {
            Player winnerPlayer = players.get(0);
            System.out.println("Congratulation!!! Game is over, Player "
                    + winnerPlayer.getName() + " won the game !!!");
            return false;
        } else {
            return true;
        }

    }

    public boolean hasThisName(List<Player> playerList, String givenName) {
        for (Player p: playerList) {
            if (p.getName().equals(givenName)) {
                return true;
            }
        }
        return false;
    }
}
