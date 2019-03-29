package Graph;

import org.graphstream.graph.Graph;

public class GraphStyle {
    public static void style(Graph graph){
        /* Display */
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        String stylesheet = "node { fill-color: red; size : 40px; }" +
                "edge { shape: line; arrow-size : 10px, 4px; }";

        graph.addAttribute("ui.stylesheet", stylesheet);

        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
    }
}
