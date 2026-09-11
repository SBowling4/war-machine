import { useEffect, useState } from 'react';
import { getPlayers } from '../services/api';

function PlayerSelector({ season, onPlayerSelect }) {
    const [players, setPlayers] = useState([]);
    const [search, setSearch] = useState('');
    const [selectedPlayer, setSelectedPlayer] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadPlayers() {
            try {
                const data = await getPlayers(season);
                setPlayers(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        }

        loadPlayers();
    }, [season]);

    function handlePlayerSelect(player) {
        setSelectedPlayer(player);
        setSearch('');
        onPlayerSelect(player);
    }

    function handleClear() {
        setSelectedPlayer(null);
        setSearch('');
        onPlayerSelect(null);
    }

    const filteredPlayers = players
        .filter((player) =>
            player.name.toLowerCase().includes(search.toLowerCase())
        )
        .slice(0, 10);

    if (loading) {
        return <p>Loading players...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div>
            <label htmlFor="player-search">Player</label>

            {selectedPlayer ? (
                <div>
                    <strong>{selectedPlayer.name}</strong>
                    <span>
            {' '}
                        — {selectedPlayer.position} — {selectedPlayer.team}
          </span>

                    <button onClick={handleClear}>
                        Change Player
                    </button>
                </div>
            ) : (
                <>
                    <input
                        id="player-search"
                        type="text"
                        placeholder="Search players..."
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                    />

                    {search && (
                        <div>
                            {filteredPlayers.length > 0 ? (
                                filteredPlayers.map((player) => (
                                    <button
                                        key={player.id}
                                        onClick={() => handlePlayerSelect(player)}
                                    >
                                        {player.name} — {player.position} — {player.team}
                                    </button>
                                ))
                            ) : (
                                <p>No players found.</p>
                            )}
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

export default PlayerSelector;