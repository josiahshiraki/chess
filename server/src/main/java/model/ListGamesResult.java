package model;
import java.util.Collection;
import java.util.List;

public record ListGamesResult(
    Collection<ListGameData> games
){}
