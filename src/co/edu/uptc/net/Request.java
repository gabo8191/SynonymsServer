package co.edu.uptc.net;

public class Request {

  private String option;
  private String word;
  private String synonym;

  public Request(String word, String synonym, String option) {
    this.word = word;
    this.synonym = synonym;
    this.option = option;
  }

  public String getOption() {
    return option;
  }

  public String getWord() {
    return word;
  }

  public String getSynonym() {
    return synonym;
  }
}
