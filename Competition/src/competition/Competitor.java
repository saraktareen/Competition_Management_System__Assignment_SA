package competition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;
import java.time.Period;

public class Competitor {

    // Competitor class
    private int competitorNumber;
    private String competitorName;
    private String email;
    private String dateOfBirth;
    private String category;
    private String level;
    private String country; // Added country property
    private List<Integer> scores;

    // Default constructor
    public Competitor() {
        // Initializes the randomized scores list
        this.scores = generateRandomScores();
    }

    // Competitor constructor for the details
    public Competitor(int competitorNumber, String competitorName, String email, String dateOfBirth,
                      String category, String level, String country) {
        this.competitorNumber = competitorNumber;
        this.competitorName = competitorName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.category = category;
        this.level = level;
        this.country = country;
        this.scores = generateRandomScores();
    }

    // Getter and setter methods for Competitor properties

    // Getter for the CompetitionNumber
    public int getCompetitorNumber() {
        return competitorNumber;
    }

    // Setter for the CompetitionNumber
    public void setCompetitorNumber(int competitorNumber) {
        this.competitorNumber = competitorNumber;
    }

    // Getter for the CompetitionName
    public String getCompetitorName() {
        return competitorName;
    }

    // Setter for the CompetitionName
    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    // Getter for the Email
    public String getEmail() {
        return email;
    }

    // Setter for the Email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for the Date of Birth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    // Setter for the Date of Birth
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter for the Category
    public String getCategory() {
        return category;
    }

    // Setter for the Category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for the Level
    public String getLevel() {
        return level;
    }

    // Setter for the Level
    public void setLevel(String level) {
        this.level = level;
    }

    // Getter for the Country
    public String getCountry() {
        return country;
    }

    // Setter for the Country
    public void setCountry(String country) {
        this.country = country;
    }

    // Getter for the Scores Array
    public List<Integer> getScores() {
        return scores;
    }

    // Instance variable for the array of Integer scores
    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    // Method to add a score to the scores list
    public void addScore(int score) {
        scores.add(score);
    }

    // Method to calculate the overall score
    public String getOverallScore() {
        double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0); // Calculates average in a stream, if there are no values, it returns 0.0
        double weightedAverage = calculateWeightedAverage();
        double averageWithoutMinMax = calculateAverageWithoutMinMax();

        return String.format("This gives him an overall score of %.1f.", averageWithoutMinMax); // Shows the overall score as a decimal value
    }

    // Method to create a string representation of the Competitor object
    @Override
    public String toString() {
        return String.format("%s is a %s aged %s and received these scores: %s%n%s",
                competitorName, level, calculateAge(), scoresAsString(), getOverallScore()); // Output of the details of the competitor
    }

    // Method to get full details of the competitor
    public String getFullDetails() {
        return String.format("%s is a %s aged %s and received these scores: %s%n%s",
                competitorName, level, calculateAge(), scoresAsString(), getOverallScore()); // Output of the details of the competitor
    }

    // Calculated the age of the competitor from the date of birth provided
    private int calculateAge() {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0; // Return 0 if date of birth is not available
        }

        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }

    // Method to get short details of the competitor
    public String getShortDetails() {
        return String.format("CN %d (%s) has overall score %d.", competitorNumber, getInitials(), Math.round(scores.stream().mapToInt(Integer::intValue).average().orElse(0.0)));
    }

    // Method to get initials from the competitor name
    private String getInitials() {
        String[] names = competitorName.split(" "); // Splits the string where there is a space
        StringBuilder initials = new StringBuilder();
        // Loops through each name in the array to append the first character
        for (String name : names) {
            initials.append(name.charAt(0));
        }
        return initials.toString().toUpperCase(); // Shows them as an upper case value
    }

    // Method to get the array of integer scores
    public int[] getScoreArray() {
        return scores.stream().mapToInt(Integer::intValue).toArray();
    }

    // Method to generate a random number of scores between 4 and 6, and each score value between 0 and 5
    private List<Integer> generateRandomScores() {
        Random random = new Random();
        int numberOfScores = random.nextInt(3) + 4; // Random number between 4 and 6
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < numberOfScores; i++) {
            scores.add(random.nextInt(6)); // Random score between 0 and 5
        }
        return scores;
    }

    // Method to convert scores to a string
    private String scoresAsString() {
        StringBuilder result = new StringBuilder();
        for (int score : scores) {
            result.append(score).append(",");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

    // Method to calculate the weighted average of scores based on levels
    private double calculateWeightedAverage() {
        double sum = 0;
        double weightSum = 0;

        for (int i = 0; i < scores.size(); i++) {
            int score = scores.get(i);
            int weight = getWeightForScoreAndLevel(score, level);
            sum += score * weight;
            weightSum += weight;
        }

        return weightSum > 0 ? sum / weightSum : 0;
    }

    // Method to calculate the average of scores disregarding the highest and lowest score
    private double calculateAverageWithoutMinMax() {
        if (scores.size() <= 2) {
            return scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }

        int minScore = scores.stream().min(Integer::compare).orElse(0);
        int maxScore = scores.stream().max(Integer::compare).orElse(0);

        int sum = scores.stream().mapToInt(Integer::intValue).sum();
        return (sum - minScore - maxScore) / (scores.size() - 2.0);
    }

    // Method to get the weight for a score based on level
    private int getWeightForScoreAndLevel(int score, String level) {
        int baseWeight = 1;
        switch (level.toLowerCase()) {
            case "novice":
                return baseWeight + 1; // Novice has higher weight
            case "intermediate":
                return baseWeight;
            case "advanced":
                return baseWeight - 1; // Advanced has lower weight
            default:
                return baseWeight;
        }
    }

    // Example usage in the main method
    public static void main(String[] args) {
        Competitor competitor = new Competitor(1, "Keith John Talbot", "keith@example.com", "2001-01-01", "Category A", "Novice", "UK");
        // Print the full details of the competitor
        System.out.println(competitor.getFullDetails());
    }
}
