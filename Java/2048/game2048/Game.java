package game2048;

import static game2048.Side.*;


/** The input/output and GUI controller for play of a game of 2048.
 *  @author Wenqu Wang*/
public class Game {

    /** Controller for a game represented by MODEL, using SOURCE as the
     *  the source of key inputs and random Tiles. */
    public Game(Model model, InputSource source) {
        _model = model;
        _source = source;
        _playing = true;
    }

    /** Return true iff we have not received a Quit command. */
    boolean playing() {
        return _playing;
    }

    /** Clear the board and play one game, until receiving a quit or
     *  new-game request.  Update the viewer with each added tile or
     *  change in the board from tilting. */
    void playGame() {
        _model.clear();
        _model.addTile(getValidNewTile());
        while (_playing) {
            if (!_model.gameOver()) {
                _model.addTile(getValidNewTile());
                _model.notifyObservers();
            }
            boolean moved;
            moved = false;
            while (!moved) {
                String cmnd = _source.getKey();
                switch (cmnd) {
                case "Quit":
                    _playing = false;
                    return;
                case "New Game":
                    return;
                case "Up": case "Down": case "Left": case "Right":
                case "\u2190": case "\u2191": case "\u2192": case "\u2193":
                    if (!_model.gameOver() && _model.tilt(keyToSide(cmnd))) {
                        _model.notifyObservers();
                        moved = true;
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    /** Return the side indicated by KEY ("Up", "Down", "Left",
     *  or "Right"). */
    private Side keyToSide(String key) {
        switch (key) {
        case "Up": case "\u2191":
            return NORTH;
        case "Down": case "\u2193":
            return SOUTH;
        case "Left": case "\u2190":
            return WEST;
        case "Right": case "\u2192":
            return EAST;
        default:
            throw new IllegalArgumentException("unknown key designation");
        }
    }

    /** Return a valid tile, using our source's tile input until finding
     *  one that fits on the current board. Assumes there is at least one
     *  empty square on the board. */
    private Tile getValidNewTile() {
        Tile newTile = _source.getNewTile(_model.size());
        int col = newTile.col();
        int row = newTile.row();
        if (_model.tile(col, row) == null) {
            return newTile;
        } else {
            return getValidNewTile();
        }
    }
 /**       Random randCol = new Random();
        Random randRow = new Random();
        while (true) {
            int col = randCol.nextInt(_model.size());
            int row = randRow.nextInt(_model.size());
            if (_model.tile(col,row) != null){
                continue;
            }
                Random rand = new Random();
                int n = rand.nextInt(4);
                int v;
                if (n == 0) {
                    v = 4;
                }
                else{
                    v = 2;
                }
                return Tile.create(v,col,row);
        }
  } */

    /** Strings representing the four arrow keys. */
    private static final String[] ARROW_KEYS = {
        "\u2191", "\u2193", "\u2190", "\u2192"
    };

    /** The playing board. */
    private Model _model;

    /** Input source from standard input. */
    private InputSource _source;

    /** True while user is still willing to play. */
    private boolean _playing;

}
