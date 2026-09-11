function MetricSelector({ metric, onMetricChange }) {
    return (
        <div>
            <label htmlFor="metric-select">Batting Metric</label>

            <select
                id="metric-select"
                value={metric}
                onChange={(event) => onMetricChange(event.target.value)}
            >
                <option value="AVG">Batting Average</option>
                <option value="OBP">On-Base Percentage</option>
                <option value="SLG">Slugging Percentage</option>
            </select>
        </div>
    );
}

export default MetricSelector;