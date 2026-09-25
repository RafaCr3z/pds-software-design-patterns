package menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import estairways.ClasseConforto;
import estairways.ESTAirways;

@SuppressWarnings("serial")
/** Janela onde se podem visualizar as informações de um voo
 */
public class JanelaVoos extends JFrame {

	// a companhia
	private ESTAirways estWays;

	/** formatador para apresentar as datas */
	private static final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");

	/** cria uma janela que lista a info dos voos
	 * @param c a companhia
	 */
	public JanelaVoos( ESTAirways c ) {
		estWays = c;
		setTitle( "EST Global Airways - Info de Voos" );
		setupJanela( );
	}
	
	/** método chamado quando o utilizador pressiona o botão de pesquisar voo.
	 * Este método deve preparar a informação do voo para ser apresentada
	 * @param codigoVoo o código do voo que o utilizador escreveu
	 */
	private void apresentarVoo( String codigoVoo ) {
		painelPassageiros.removeAll();

		// TODO ver qual o voo associado ao código e se não houver avisar
		if( Math.abs(2) == 5 /* TODO colocar o teste correto */  ) {
			JOptionPane.showMessageDialog(this, "Não há voos com o código " + codigoVoo, "Voo desconhecido", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// TODO chamar o método com a informação correta
		setVooInfo( "código voo", "código origem", "código destino", LocalDateTime.now() /* data partida */);
		
		// TODO para classe de conforto indicar a info correta
		addClasseInfo( "nome da classe", 3 /* lugares disponíveis*/, 7 /* total de lugares*/);
		
		painelResumoVoo.setVisible( true );
	}
	
	/** método chamado quando o utilizador pressiona o botão
	 * de ver o manifesto de passageiros
	 */
	private void mostrarListaPassageiros() {
		// TODO criar um vector com os nomes de todos os passageiros deste voo
		Vector<String> nomes = new Vector<String>();
		nomes.add( "Orville Wright" );
		nomes.add( "Wilbur Wright" );
		
		JList<String> lista = new JList<String>( nomes );
		String vooCode = "código voo"; // TODO colocar o código do voo
		JOptionPane.showMessageDialog(this, new JScrollPane( lista ),"Passageiros no voo " + vooCode, JOptionPane.INFORMATION_MESSAGE );		
	}
	
	/** método a chamar para apresentar a informação de uma classe de conforto
	 * @param nomeClasse nome da classe
	 * @param lugaresDisponiveis lugares ainda disponíveis nesta classe
	 * @param lugaresTotais lugares totais nesta classe
	 */
	private void addClasseInfo(String nomeClasse, int lugaresDisponiveis, int lugaresTotais) {
		painelPassageiros.add( new PainelClasseConforto(nomeClasse, lugaresDisponiveis+" / " + lugaresTotais ) );		
	}

	/** Método para retornar o nome a usar na interface para a classe de conforto
	 * Este método podia estar na enumeração, mas depois não seria internacionalizável
	 * @param c a classe de conforto
	 * @return o nome a usar na interface
	 */
	private String getNomeClasse(ClasseConforto c) {
		return switch( c ) {
		case STANDARD -> "Standard";
		case COMFORT -> "Confort";
		case DELUXE -> "Deluxe";
		};
	}

	/** método a chamar para apresentar a informação de um voo
	 * @param codigo código do voo
	 * @param aeroOrigem código do aeroporto de origem
	 * @param aeroDestino código do aeroporto de destino
	 * @param partida data de partida
	 */
	private void setVooInfo( String codigo, String aeroOrigem, String aeroDestino, LocalDateTime partida) {
		viagemLbl.setText( aeroOrigem + " » " + aeroDestino );
		dataBt.setText( partida.format(dataFormatter) );
		codigoLbl.setText( codigo );
	}
	
	// variáveis para os elementos gráficos
	private JPanel reservaInfoPanel;
	private JTextField codigoTf;
	private JPanel painelPassageiros;
	private JLabel viagemLbl;
	private JLabel dataBt;
	private JLabel codigoLbl;
	private JPanel painelResumoVoo;

	private void setupJanela( ) {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPanel escolhas = setupPainelPesquisa( );
		getContentPane().add( escolhas, BorderLayout.NORTH );
		painelResumoVoo = new JPanel( new BorderLayout() );
		reservaInfoPanel = setupPainelGerirReserva();
		JPanel infoVoo = setupPainelInfoVoo();
		painelResumoVoo.add( infoVoo, BorderLayout.NORTH );
		painelResumoVoo.add( reservaInfoPanel, BorderLayout.CENTER );
		painelResumoVoo.setVisible( false );
		getContentPane().add( painelResumoVoo, BorderLayout.CENTER );
		setSize( 400, 280 );
	}

	private JPanel setupPainelPesquisa( ) {
		JPanel painel = new JPanel( );
		painel.setBorder( BorderFactory.createTitledBorder("Escolher Voo"));
		
		painel.add( new JLabel("Referência Voo: ") );
		
		codigoTf = new JTextField( 10 );
		painel.add( codigoTf );
		
		JButton procuraBt = new JButton( "Pesquisar" );
		painel.add( procuraBt );
		
		procuraBt.addActionListener( e -> {
			String idr = codigoTf.getText().trim();
			if( idr.isBlank() || idr.length() < 4 )
				JOptionPane.showMessageDialog(this, "Voo tem de ter mais de 4 caracteres");
			else {				
				apresentarVoo( idr );
				reservaInfoPanel.setVisible( true );
			}
		} );

		return painel;
	}
	
	// variáveis da interface que estão aqui para não "atrapalhar"
	private static Font grandeFont = new Font("ROMAN", Font.BOLD, 16 );
	private static Font mediaFontBold = new Font("ROMAN", Font.BOLD, 13 );
	//private static Font mediaFont = new Font("ROMAN", Font.PLAIN, 13 );

	private JPanel setupPainelGerirReserva() {
		JPanel panel = new JPanel( new BorderLayout() );
		panel.add( setupPainelInfoPassageiros() );
		return panel;
	}
	
	private JPanel setupPainelInfoVoo() {
		SpringLayout layout = new SpringLayout();
		JPanel painel = new JPanel( layout );
		painel.setBorder( new TitledBorder("Voo") );
		viagemLbl = new JLabel("Aeroporto Origem » Aeroporto Destino", JLabel.CENTER);
		viagemLbl.setFont( grandeFont );
		painel.add( viagemLbl );
		
		dataBt = new JLabel("23-23-2024  10:50");
		dataBt.setFont( mediaFontBold );		
		painel.add( dataBt );
		
		codigoLbl = new JLabel("AA0023", JLabel.CENTER);
		codigoLbl.setFont( mediaFontBold );
		
		layout.putConstraint( SpringLayout.NORTH, viagemLbl, 5, SpringLayout.NORTH, painel );
		layout.putConstraint( SpringLayout.WEST, viagemLbl, 5, SpringLayout.WEST, painel );
		layout.putConstraint( SpringLayout.EAST, viagemLbl, -5, SpringLayout.HORIZONTAL_CENTER, painel );
		
		layout.putConstraint( SpringLayout.BASELINE, dataBt, 0, SpringLayout.BASELINE, viagemLbl );
		layout.putConstraint( SpringLayout.WEST, dataBt, 5, SpringLayout.EAST, viagemLbl );
		layout.putConstraint( SpringLayout.EAST, dataBt, -5, SpringLayout.EAST, painel );

		layout.putConstraint( SpringLayout.SOUTH, painel, 5, SpringLayout.SOUTH, viagemLbl );
		return painel;
	}
	
	private JPanel setupPainelInfoPassageiros( ) {
		JPanel painel = new JPanel( new BorderLayout() );
		painel.setBorder( new TitledBorder("Lugares disponíveis") );
		painelPassageiros = new PainelListador();
		painel.add( painelPassageiros, BorderLayout.CENTER );
		JButton listaBt = new JButton("Manifesto passageiros");
		listaBt.addActionListener( a -> mostrarListaPassageiros() );
		
		painel.add( listaBt, BorderLayout.SOUTH );
		return painel;
	}
	
	private class PainelClasseConforto extends JPanel {
		JLabel nomeLbl, lugaresLbl;  
		
		public PainelClasseConforto( String nome, String lugaresInfo ) {
			this.nomeLbl = new JLabel( nome );
			this.lugaresLbl = new JLabel( lugaresInfo );

			SpringLayout layout = new SpringLayout();
			setLayout( layout );
			add( nomeLbl );
			add( lugaresLbl );
			
			layout.putConstraint( SpringLayout.NORTH, nomeLbl, 2, SpringLayout.NORTH, this );
			layout.putConstraint( SpringLayout.WEST, nomeLbl, 5, SpringLayout.WEST, this );
			layout.putConstraint( SpringLayout.EAST, nomeLbl, 0, SpringLayout.HORIZONTAL_CENTER, this );

			layout.putConstraint( SpringLayout.BASELINE, lugaresLbl, 0, SpringLayout.BASELINE, nomeLbl );
			layout.putConstraint( SpringLayout.WEST, lugaresLbl, 5, SpringLayout.EAST, nomeLbl );
			layout.putConstraint( SpringLayout.EAST, lugaresLbl, -5, SpringLayout.EAST, this );

			layout.putConstraint( SpringLayout.SOUTH, this, 2, SpringLayout.SOUTH, lugaresLbl );
		}
	}
}


