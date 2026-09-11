import { useState } from 'react';
import PlayerSelector from './components/PlayerSelector';
import MetricSelector from './components/MetricSelector';
import { calculateWar } from './services/api';

function App() {
    const [selectedPlayer, setSelectedPlayer] = useState(null);
    const [metric, setMetric] = useState('AVG');
    const [warResult, setWarResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    async function handleCalculateWar() {
        if (!selectedPlayer) {
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const data = await calculateWar(
                selectedPlayer.id,
                2025,
                metric
            );

            setWarResult(data);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h1>WAR Calculator</h1>

            <PlayerSelector
                season={2025}
                onPlayerSelect={(player) => {
                    setSelectedPlayer(player);
                    setWarResult(null);
                }}
            />

            <MetricSelector
                metric={metric}
                onMetricChange={(newMetric) => {
                    setMetric(newMetric);
                    setWarResult(null);
                }}
            />

            <button onClick={handleCalculateWar}>
                {loading ? 'Calculating...' : 'Calculate WAR'}
            </button>

            {error && (
                <p>Error: {error}</p>
            )}

            {warResult && (
                <div>
                    <h2>WAR Result</h2>

                    <h3>{selectedPlayer.name}</h3>
                    <p>
                        {selectedPlayer.position} — {selectedPlayer.team}
                    </p>

                    <p>
                        <strong>Batting Metric:</strong>{' '}
                        {warResult.battingMetric}
                    </p>

                    <hr />

                    <h3>WAR Breakdown</h3>

                    <p>
                        <strong>Batting Runs:</strong>{' '}
                        {warResult.battingRuns.toFixed(2)}
                    </p>

                    <p>
                        <strong>Baserunning Runs:</strong>{' '}
                        {warResult.baserunningRuns.toFixed(2)}
                    </p>

                    <p>
                        <strong>Fielding Runs:</strong>{' '}
                        {warResult.fieldingRuns.toFixed(2)}
                    </p>

                    <p>
                        <strong>Replacement Runs:</strong>{' '}
                        {warResult.replacementRuns.toFixed(2)}
                    </p>

                    <hr />

                    <p>
                        <strong>Runs Above Replacement:</strong>{' '}
                        {warResult.runsAboveReplacement.toFixed(2)}
                    </p>

                    <h2>
                        WAR: {warResult.war.toFixed(2)}
                    </h2>
                </div>
            )}
        </div>
    );
}

export default App;