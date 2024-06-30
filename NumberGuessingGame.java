import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        int maxAttempts = 10;
        int score = 0;
        int rounds = 0;
        boolean playAgain = true;

        System.out.println("Welcome to the Number Guessing Game!");
        
        while (playAgain) {
            int number = random.nextInt(100) + 1;
            int attempts = 0;
            boolean guessedCorrectly = false;
            
            System.out.println("\nI have generated a number between 1 and 100.");
            System.out.println("You have " + maxAttempts + " attempts to guess it.");
            
            while (attempts < maxAttempts) {
                System.out.print("Attempt " + (attempts + 1) + ": Enter your guess: ");
                int guess = scanner.nextInt();
                attempts++;
                
                if (guess < number) {
                    System.out.println("Too low!");
                } else if (guess > number) {
                    System.out.println("Too high!");
                } else {
                    System.out.println("Congratulations! You've guessed the correct number!");
                    guessedCorrectly = true;
                    score += (maxAttempts - attempts + 1); 
                    break;
                }
            }
            
            if (!guessedCorrectly) {
                System.out.println("Sorry, you've used all your attempts. The correct number was " + number + ".");
            }
            
            rounds++;
            System.out.println("Your current score: " + score + " points.");
            System.out.println("Rounds played: " + rounds);
            
            System.out.print("Do you want to play again? (yes/no): ");
            playAgain = scanner.next().equalsIgnoreCase("yes");
        }

        System.out.println("Thank you for playing! Your final score is " + score + " points after " + rounds + " rounds.");
        
    }
}
