package step.project;

import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.TA_Grid;
import de.vandermeer.asciithemes.TA_GridConfig;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.asciithemes.u8.U8_Grids;
import de.vandermeer.skb.interfaces.render.*;
import de.vandermeer.skb.interfaces.strategies.collections.list.ArrayListStrategy;
import de.vandermeer.skb.interfaces.transformers.ClusterElementTransformer;
import de.vandermeer.skb.interfaces.transformers.StrBuilder_To_String;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import de.vandermeer.skb.interfaces.transformers.textformat.Text_To_FormattedText;
import de.vandermeer.translation.targets.Text2Html;
import de.vandermeer.translation.targets.Text2Latex;
import org.stringtemplate.v4.ST;
import org.w3c.dom.ls.LSOutput;
import step.project.flight.flightDAO.FlightController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainTEST {
    public static void main(String[] args) {
        //showOutput();

        FlightController flightController = new FlightController("flight.dat");

        flightController.getAllFlights().forEach(System.out::println);
    }


//    public static void showOutput(){
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow(null, null, null, null, "span all 5 columns");
//        at.addRule();
//        at.addRow(null, null, null, "span 4 columns", "just 1 column");
//        at.addRule();
//        at.addRow(null, null, "span 3 columns", null, "span 2 columns");
//        at.addRule();
//        at.addRow(null, "span 2 columns", null, null, "span 3 columns");
//        at.addRule();
//        at.addRow("just 1 column", null, null, null, "span 4 columns");
//        at.addRule();
//        at.addRow("just 1 column", "just 1 column", "just 1 column", "just 1 column", "just 1 column");
//        at.addRule();
//        System.out.println(at.render(71));
//        // end::example[]
//    }

    public static void showOutput() {

//
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow("rc 11", "rc 12");
//        at.addRule();
//        at.addRow("rc 21", "rc 22");
//        at.addRule();
//        at.getContext().setWidth(13);
//
//        System.out.println(at.render());
//
//        at.getContext().setGrid(A7_Grids.minusBarPlusEquals());
//        System.out.println(at.render());
//
//        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
//        System.out.println(at.render());
//
//        at.getContext().setGrid(U8_Grids.borderDoubleLight());
//        System.out.println(at.render());
//
//        at.getContext().setGrid(U8_Grids.borderDouble());
//        System.out.println(at.render());
//        // end::example[]
//┌─────┬─────┐
//│rc 11│rc 12│
//├─────┼─────┤
//│rc 21│rc 22│
//└─────┴─────┘
//+-----+-----+
//|rc 11|rc 12|
//+-----+-----+
//|rc 21|rc 22|
//+-----+-----+
//═════════════
// rc 11 rc 12
//═════════════
// rc 21 rc 22
//═════════════
//╒═════╤═════╕
//│rc 11│rc 12│
//╞═════╪═════╡
//│rc 21│rc 22│
//╘═════╧═════╛
//╔═════╦═════╗
//║rc 11║rc 12║
//╠═════╬═════╣
//║rc 21║rc 22║
//╚═════╩═════╝


//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        String text = "sdf sdf srwer etyety we5r erhrtuey tterwfsdfbtr jryerw";
//        at.addRule();
//        at.addRow(text, text, text);
//        at.addRule();
//        at.addRow(text, text, text);
//        at.addRule();
//        at.setTextAlignment(TextAlignment.RIGHT);
//        System.out.println(at.render(76));
//        // end::example[]

//┌────────────────────────┬────────────────────────┬────────────────────────┐
//│    sdf sdf srwer etyety│    sdf sdf srwer etyety│    sdf sdf srwer etyety│
//│           we5r erhrtuey│           we5r erhrtuey│           we5r erhrtuey│
//│     tterwfsdfbtr jryerw│     tterwfsdfbtr jryerw│     tterwfsdfbtr jryerw│
//├────────────────────────┼────────────────────────┼────────────────────────┤
//│    sdf sdf srwer etyety│    sdf sdf srwer etyety│    sdf sdf srwer etyety│
//│           we5r erhrtuey│           we5r erhrtuey│           we5r erhrtuey│
//│     tterwfsdfbtr jryerw│     tterwfsdfbtr jryerw│     tterwfsdfbtr jryerw│
//└────────────────────────┴────────────────────────┴────────────────────────┘
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow("first row (col1)", "text (col2)", "more text (col3)", "even more (col4)");
//        at.addRule();
//        at.addRow("second row (col1)", "text (col2)", "more text (col3)", "even more (col4)");
//        at.addRule();
//        System.out.println(at.render());
//        // end::example[]
//┌───────────────────┬───────────────────┬───────────────────┬──────────────────┐
//│first row (col1)   │text (col2)        │more text (col3)   │even more (col4)  │
//├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
//│second row (col1)  │text (col2)        │more text (col3)   │even more (col4)  │
//└───────────────────┴───────────────────┴───────────────────┴──────────────────┘
//
//        // tag::example[]
//        ST st = new ST("sdf sdfsdfg wrtwert yewrt thtj 5653t erthey weyeruy wwert wyw");
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow(st);
//        at.addRule();
//        System.out.println(at.render());
//        // end::example[]
//┌──────────────────────────────────────────────────────────────────────────────┐
//│sdf sdfsdfg wrtwert yewrt thtj 5653t erthey weyeruy wwert wyw                 │
//└──────────────────────────────────────────────────────────────────────────────┘
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        String text = "sdf sdf sdf sdf ef eht er rg e gd hd yjer wrfd vbhfdg sdf";
//        at.addRule();
//        at.addRow(text, text, text);
//        at.addRule();
//        AT_Row row = at.addRow(text, text, text);
//        at.addRule();
//        row.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
//        System.out.println(at.render(76));
//        // end::example[]
//┌────────────────────────┬────────────────────────┬────────────────────────┐
//│sdf sdf sdf sdf  ef  eht│sdf sdf sdf sdf  ef  eht│sdf sdf sdf sdf  ef  eht│
//│er rg e gd hd yjer  wrfd│er rg e gd hd yjer  wrfd│er rg e gd hd yjer  wrfd│
//│vbhfdg sdf              │vbhfdg sdf              │vbhfdg sdf              │
//├────────────────────────┼────────────────────────┼────────────────────────┤
//│sdf sdf sdf sdf  ef  eht│sdf sdf sdf sdf  ef  eht│  sdf sdf sdf sdf ef eht│
//│er rg e gd hd yjer  wrfd│er rg e gd hd yjer  wrfd│ er rg e gd hd yjer wrfd│
//│vbhfdg sdf              │vbhfdg sdf              │              vbhfdg sdf│
//└────────────────────────┴────────────────────────┴────────────────────────┘
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        AT_Row row1 = at.addRow("first", "information");
//        at.addRule();
//        AT_Row row2 = at.addRow("second", "info");
//        at.addRule();
//
//        at.getRenderer().setCWC(new CWC_LongestWord());
//        System.out.println(at.render());
//
//        at.setPaddingLeftRight(1);
//        System.out.println(at.render());
//
//        at.setPaddingLeftRight(0);
//        row1.getCells().get(0).getContext().setPaddingLeftRight(2);
//        row1.getCells().get(1).getContext().setPaddingLeftRight(3);
//        row2.getCells().get(0).getContext().setPaddingLeftRight(3);
//        row2.getCells().get(1).getContext().setPaddingLeftRight(4);
//        System.out.println(at.render());
//        // end::example[]
//┌──────┬───────────┐
//│first │information│
//├──────┼───────────┤
//│second│info       │
//└──────┴───────────┘
//┌────────┬─────────────┐
//│ first  │ information │
//├────────┼─────────────┤
//│ second │ info        │
//└────────┴─────────────┘
//┌────────────┬─────────────────┐
//│  first     │   information   │
//├────────────┼─────────────────┤
//│   second   │    info         │
//└────────────┴─────────────────┘
//
//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        String text = "ertyu hgfsdf cdvsd bnjh gftr sdf sdf sdf wererer weeweer wer sefrser werwer wer sdf sdfsdf sd  xcvx nhjhv";
//        AT_Row row;
//        at.addRule();
//        row = at.addRow(text, text, text);
//        row.getCells().get(0).getContext().setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
//        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.JUSTIFIED);
//        row.getCells().get(2).getContext().setTextAlignment(TextAlignment.JUSTIFIED_RIGHT);
//        at.addRule();
//        row = at.addRow(text, text, text);
//        row.getCells().get(0).getContext().setTextAlignment(TextAlignment.LEFT);
//        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.CENTER);
//        row.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
//        at.addRule();
//        System.out.println(at.render(79));
//        // end::example[]
//┌─────────────────────────┬─────────────────────────┬─────────────────────────┐
//│ertyu hgfsdf  cdvsd  bnjh│ertyu hgfsdf  cdvsd  bnjh│ertyu hgfsdf  cdvsd  bnjh│
//│gftr sdf sdf sdf  wererer│gftr sdf sdf sdf  wererer│gftr sdf sdf sdf  wererer│
//│weeweer    wer    sefrser│weeweer    wer    sefrser│weeweer    wer    sefrser│
//│werwer wer sdf sdfsdf  sd│werwer wer sdf sdfsdf  sd│werwer wer sdf sdfsdf  sd│
//│xcvx nhjhv               │xcvx                nhjhv│               xcvx nhjhv│
//├─────────────────────────┼─────────────────────────┼─────────────────────────┤
//│ertyu hgfsdf cdvsd bnjh  │ ertyu hgfsdf cdvsd bnjh │  ertyu hgfsdf cdvsd bnjh│
//│gftr sdf sdf sdf wererer │gftr sdf sdf sdf wererer │ gftr sdf sdf sdf wererer│
//│weeweer wer sefrser      │   weeweer wer sefrser   │      weeweer wer sefrser│
//│werwer wer sdf sdfsdf sd │werwer wer sdf sdfsdf sd │ werwer wer sdf sdfsdf sd│
//│xcvx nhjhv               │       xcvx nhjhv        │               xcvx nhjhv│
//└─────────────────────────┴─────────────────────────┴─────────────────────────┘


//        // tag::example[]
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow("first", "information");
//        at.addRule();
//        at.addRow("second", "info");
//        at.addRule();
//
//        at.getRenderer().setCWC(new CWC_LongestWordMin(9));
//        System.out.println(at.render());
//
//        at.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1,30}));
//        System.out.println(at.render());
//┌─────────┬───────────┐
//│first    │information│
//├─────────┼───────────┤
//│second   │info       │
//└─────────┴───────────┘
//┌──────┬──────────────────────────────┐
//│first │information                   │
//├──────┼──────────────────────────────┤
//│second│info                          │
//└──────┴──────────────────────────────┘


//
//        // tag::example[]
//        String text = "A sentence with some normal text, not specific to LaTeX. " +
//                "Now for some characters that require conversion: # % &. " +
//                "And some more: © § ¤. " +
//                "And even more: È É Ê Ë. " +
//                "And some arrows as well: ← ↑ → ↓ ↔"
//                ;
//
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow(text, text).getCells().get(1).getContext().setTargetTranslator(new Text2Latex());
//        at.addRule();
//        at.setTextAlignment(TextAlignment.LEFT);
//
//        System.out.println(at.render());
//
/**┌───────────────────────────────────────┬──────────────────────────────────────┐
 // │A sentence with some normal text, not  │A sentence with some normal text, not │
 // │specific to LaTeX. Now for some        │specific to LaTeX. Now for some       │
 // │characters that require conversion: # %│characters that require conversion: \#│
 // │&. And some more: © § ¤. And even more:│\% \&. And some more: {\copyright}    │
 // │È É Ê Ë. And some arrows as well: ← ↑ →│{\S} \currency. And even more: \`{E}  │
 // │↓ ↔                                    │\'{E} \^{E} \"{E}. And some arrows as │
 // │                                       │well: \(\\\) \(                       │
 // │                                       │\(\                                   │
 // └───────────────────────────────────────┴──────────────────────────────────────┘
 */
//

//
//
//        AsciiTable at = new AsciiTable();
//        at.addRule();
//        at.addRow("rc 11", "rc 12");
//        at.addLightRule();
//        at.addRow("rc 21", "rc 22");
//        at.addStrongRule();
//        at.addRow("rc 31", "rc 32");
//        at.addHeavyRule();
//        at.getContext().setWidth(13);
//
//        at.getContext().setGrid(A8_Grids.lineDoubleBlocks());
//        System.out.println(at.render());
////        // end::example[]
//═════════════
// rc 11 rc 12
//─────────────
// rc 21 rc 22
//▓▓▓▓▓▓▓▓▓▓▓▓▓
// rc 31 rc 32
//▀▀▀▀▀▀▀▀▀▀▀▀▀
    }
}
