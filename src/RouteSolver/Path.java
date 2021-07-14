package RouteSolver;

public class Path {
	int nodeStart;
	int nodeEnd;
	int visits;
	
	public Path(int nodeStart, int nodeEnd, int visits) {
		this.nodeStart = nodeStart;
		this.nodeEnd = nodeEnd;
		this.visits = visits;
	}
	
	public Path(Path path) {
		nodeStart = path.nodeStart;
		nodeEnd = path.nodeEnd;
		visits = path.visits;
	}
}
