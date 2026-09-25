package menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import estairways.ESTAirways;


@SuppressWarnings("serial")
/** Classe que representa a janela onde se podem gerir
 *  os vários elementos de uma reserva
 */
public class JanelaReservas extends JFrame {
	// a companhia
	private ESTAirways estWays;

	// formatador para as dats e horas
	private static final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	/** construtor da janela de fazer reserva
	 * @param c a companhia
	 */
	public JanelaReservas( ESTAirways c ) {
		estWays = c;
		setTitle( "EST Global Airways - Gestão de reservas" );
		setupJanela( );
	}
	
	/** método chamado quando o utilizador pressiona o botão de pesquisar reserva.
	 * Este método deve preparar a informação da reserva para ser apresentada
	 * @param reservaRef o identificador de reserva que o utilizador escreveu
	 */
	private void apresentarReserva( String reservaRef ) {
		clearReservaInfo();
		
		// testar se há reserva, senão avisar e sair
		if( Math.abs(2) == 5 /* TODO substituir pelo teste certo!*/) {
			JOptionPane.showMessageDialog(this, "Não há reservas com a referência " + reservaRef, "Reserva Desconhecida", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// TODO chamar o método com a informação correta
		setVooInfo( "Código voo", "nome origem", "nome destino", LocalDateTime.now() /*data partida*/);
		
		// TODO para cada passageiro da reserva, chamar o método com a informação correta
		addPassageiroInfo( 0 /*número do passageiro*/, "Nome do passageiro", "lugar", 1 /* nº bagagens porão*/ );
		
		// apresentar a parte da reserva  
		painelResumoReserva.setVisible( true );
	}
	
	/** chamar este voo para apresentar a informação sobre o voo
	 * @param codigo código do voo
	 * @param aeroOrigem nome do aeroporto de origem
	 * @param aeroDestino nome do aeroporto de destino
	 * @param partida data de partida
	 */
	private void setVooInfo( String codigo, String aeroOrigem, String aeroDestino, LocalDateTime partida) {
		viagemLbl.setText( aeroOrigem + " » " + aeroDestino );
		dataBt.setText( partida.format(dataFormatter) );
		codigoLbl.setText( codigo );
	}
	
	/** chamar este método para cada passageiro presente na reserva
	 * @param num número do passageiro
	 * @param nome nome do passageiro
	 * @param lugar lugar onde está o passageiro (null se não tiver lugar reservado)
	 * @param numBagagemPorao número de bagagens de porão associadas ao passageiro 
	 */
	private void addPassageiroInfo(int num, String nome, String lugar, int numBagagemPorao) {
		PainelPassageiro pp = new PainelPassageiro(num, nome, lugar != null? lugar: "Sem lugar", numBagagemPorao);
		painelPassageiros.add( pp );
		
	}
	
	/** método chamado quando se pressiona a data de reserva para a mudar
	 */
	private void mudarDataReserva( ) {
		// ver se permite a alteração de data
		if( Math.abs(2 ) == 5 /* TODO substituir pelo teste correto */) {
			JOptionPane.showMessageDialog(this, "A sua reserva não permite a alteração da data");
			return;
		}
		
		// TODO calcular custo total
		long custo = 100L;
		if( custo != 0 )
			if( JOptionPane.showConfirmDialog( this, "Esta operação tem um custo de " + custo + "! Deseja continuar?",
					                           "Confirmar Operação", JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION )
				return;
		
		JOptionPane.showMessageDialog(this, "A alteração da data foi efetuada! (vamos assumir que sim, pelo menos)");
		
	}

	/** método chamado quando se pressiona o nome do passageiro para o mudar
	 * @param num número do passageiro a mudar de nome
	 * @return o novo nome a usar (ou o antigo se não houver mudança)
	 */
	private String mudarNomePassageiro(int num) {
		String nomeAntigo = "Nome antigo"; // TODO ver qual era o valor anterior
		
		// ver se pode alterar o nome do passageiro
		if( Math.abs(2) == 5 /* TODO colocar o teste correto */ ) {
			JOptionPane.showMessageDialog(this, "A sua reserva não permite alterações nos passageiros");
			return nomeAntigo;
		}
		
		// ver se há custos pela operação
		long custo = 10;
		if( custo != 0 )
			if( JOptionPane.showConfirmDialog( this, "Esta operação tem um custo de " + custo + "! Deseja continuar?",
					                           "Confirmar Operação", JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION )
				return nomeAntigo;
		
		String novoNome = JOptionPane.showInputDialog("Introduza o novo nome", nomeAntigo);
		if( novoNome == null ) // cancelou a operação
			return nomeAntigo;
		if( novoNome.isBlank() ) {
			JOptionPane.showMessageDialog(this, "Nome não pode ser vazio. Operação cancelada");
			return nomeAntigo;
		}
		if( novoNome.equals(nomeAntigo) )
			return nomeAntigo;

		// TODO alterar a info da reserva
		return novoNome;
	}

	/** método chamado quando se pressiona o lugar do passageiro para o mudar
	 * @param num número do passageiro a mudar de lugar
	 * @return o novo lugar a usar (ou o antigo se não houver mudança)
	 */
	private String reservarLugarPassageiro(int numPass ) {
		String lugarAntigo = "Lugar antigo"; // TODO ver qual era o valor anterior
		
		// testar se pode alterar o lugar
		if( Math.abs(2) == 5 /* TODO colocar o teste correto */  ) {
			JOptionPane.showMessageDialog(this, "A sua reserva não permite reservar lugares");
			return lugarAntigo;
		}
		
		// TODO calcular o custo
		long custo = 15;
		if( custo != 0 )
			if( JOptionPane.showConfirmDialog( this, "Esta operação tem um custo de " + custo + "! Deseja continuar?",
					                           "Confirmar Operação", JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION )
				return lugarAntigo;
		
		String[] lugares = listaLugaresVoo();		
		String novoLugar = (String)JOptionPane.showInputDialog(this, "Escolha o novo lugar", "Reserva lugares", JOptionPane.QUESTION_MESSAGE, null, lugares, lugarAntigo);
		if( novoLugar == null || novoLugar.equals(lugarAntigo) ) // cancelou a operação ou não alterou o lugar
			return lugarAntigo;
		
		// TODO processar a alteração 
		return novoLugar;
	}

	/** método chamado quando se pressiona o número de bagagens de porão de um passageiro para o mudar
	 * @param num número do passageiro a mudar o número de bagagens de porão
	 * @return o novo número de bagagens a usar (ou o antigo se não houver mudança)
	 */
	private int mudarBagagens( int numPass ) {
		int numAntigo = 0; // TODO ver qual o valor anterior
		
		// TODO ver qual o máximo de bagagens
		int maxBags = 0;
		Integer[] malasOp = IntStream.rangeClosed(0, maxBags).boxed().toArray( Integer[]::new);
		Integer novoNum = (Integer)JOptionPane.showInputDialog(this, "Quantas bagagens de porão deseja?", "Resgistar bagagem", JOptionPane.QUESTION_MESSAGE, null, malasOp, numAntigo);
		if( novoNum == null || novoNum == numAntigo ) // cancelado ou manteve a seleção
			return numAntigo;
		
		// TODO ver o custo
		long custo = 20;
		if( custo > 0 ) {
			if( JOptionPane.showConfirmDialog( this, "Esta operação tem um custo de " + custo + "! Deseja continuar?",
                    "Confirmar Operação", JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION )
				return numAntigo;
		}
		
		// TODO processar a alteração
		return novoNum;
	}
	
	/** método chamado quando o utilizador pressiona o botão de ver reservas.
	 * Este método deve colocar numa janela todas as reservas existentes no sistema
	 */
	private void mostrarTodasReservas() {
		Vector<String> listaIds = new Vector<>();
		// TODO preeencher o vector com todos os ids de reservas do sistema

		JList<String> list = new JList<String>( listaIds );
		list.addListSelectionListener( e -> { if( list.getSelectedIndex() != -1)
			                                     referenciaTf.setText( list.getSelectedValue() ); } );
		
		JOptionPane.showMessageDialog( this, list );
	}
	
	/** método chamado quando se pressiona o botão de cancelar reserva
	 */
	public void cancelarReserva( ) {
		// TODO calcular o reembolso
		long reembolso = 0;
		if( JOptionPane.showConfirmDialog( this, "Recebe um reembolso de " + reembolso + "! Deseja continuar?",
					                           "Confirmar Operação", JOptionPane.YES_NO_OPTION ) != JOptionPane.YES_OPTION )
			return;

		// TODO processar a alteração
		painelResumoReserva.setVisible( false );		
	}

	/** método auxiliar para simular uma lista de lugares
	 * @return a lista de lugares do voo
	 */
	private String[] listaLugaresVoo() {
		int filas = 10, lugaresPorFila = 4; 
		String lugares[] = new String[ filas * lugaresPorFila ];
		for( int f = 0; f < filas; f++ )
			for( int l = 0; l < lugaresPorFila; l++ ) {
				lugares[f*lugaresPorFila + l] = "" + (f+1) + (char)('A'+l);
			}
		return lugares;
	}
	
	// variáveis para os componente gráficos
	private JPanel reservaInfoPanel;
	private JTextField referenciaTf;
	private JPanel painelPassageiros;
	private JLabel viagemLbl;
	private JButton dataBt;
	private JLabel codigoLbl;
	private JPanel painelResumoReserva;


	/** prepara a janela com os vários componentes visuais
	 */
	private void setupJanela( ) {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPanel escolhas = setupPainelReserva( );
		getContentPane().add( escolhas, BorderLayout.NORTH );
		painelResumoReserva = new JPanel( new BorderLayout() );
		reservaInfoPanel = setupPainelGerirReserva();
		JPanel infoVoo = setupPainelInfoVoo();
		painelResumoReserva.add( infoVoo, BorderLayout.NORTH );
		painelResumoReserva.add( reservaInfoPanel, BorderLayout.CENTER );
		JButton cancelBt = new JButton("Cancelar reserva");
		cancelBt.addActionListener( a -> cancelarReserva() );
		painelResumoReserva.add( cancelBt, BorderLayout.SOUTH );
		painelResumoReserva.setVisible( false );
		getContentPane().add( painelResumoReserva, BorderLayout.CENTER );
		setSize( 400, 450 );
	}

	private JPanel setupPainelReserva( ) {
		JPanel painel = new JPanel( );
		painel.setBorder( BorderFactory.createTitledBorder("Escolher Reserva"));
		
		painel.add( new JLabel("Referência reserva: ") );
		
		referenciaTf = new JTextField( 10 );
		painel.add( referenciaTf );
		
		JButton procuraBt = new JButton( "Pesquisar" );
		painel.add( procuraBt );
		
		procuraBt.addActionListener( e -> {
			String idr = referenciaTf.getText().trim();
			if( idr.isBlank() || idr.length() != 6 )
				JOptionPane.showMessageDialog(this, "Referência tem de ter 6 caracteres");
			else {				
				apresentarReserva( idr );
				reservaInfoPanel.setVisible( true );
			}
		} );

		JButton verTodasBt = new JButton("Ver");
		verTodasBt.addActionListener( e -> mostrarTodasReservas() );
		painel.add( verTodasBt );
		
		return painel;
	}
	
	private void clearReservaInfo() {
		painelPassageiros.removeAll();
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
		
		//JLabel dataLbl = new JLabel("23-23-2024  10:50", JLabel.CENTER);
		dataBt = new JButton("23-23-2024  10:50");
		dataBt.setOpaque( false );
		dataBt.setBorderPainted( false );
		dataBt.setContentAreaFilled( false );
		dataBt.setFont( mediaFontBold );		
		dataBt.getModel().addChangeListener( c -> dataBt.setForeground( dataBt.getModel().isRollover()? Color.BLUE: Color.BLACK ) );
		dataBt.setToolTipText("Mudar a data do voo");
		dataBt.addActionListener( a -> mudarDataReserva() );
		painel.add( dataBt );
		
		codigoLbl = new JLabel("AA0023", JLabel.CENTER);
		codigoLbl.setFont( mediaFontBold );
		painel.add( codigoLbl );
		
		layout.putConstraint( SpringLayout.NORTH, viagemLbl, 5, SpringLayout.NORTH, painel );
		layout.putConstraint( SpringLayout.WEST, viagemLbl, 5, SpringLayout.WEST, painel );
		layout.putConstraint( SpringLayout.EAST, viagemLbl, -5, SpringLayout.EAST, painel );
		
		layout.putConstraint( SpringLayout.NORTH, dataBt, 5, SpringLayout.SOUTH, viagemLbl );
		layout.putConstraint( SpringLayout.WEST, dataBt, 0, SpringLayout.WEST, viagemLbl );
		layout.putConstraint( SpringLayout.EAST, dataBt, 0, SpringLayout.HORIZONTAL_CENTER, viagemLbl );

		layout.putConstraint( SpringLayout.BASELINE, codigoLbl, 0, SpringLayout.BASELINE, dataBt );
		layout.putConstraint( SpringLayout.EAST, codigoLbl, 0, SpringLayout.EAST, viagemLbl );
		layout.putConstraint( SpringLayout.WEST, codigoLbl, 0, SpringLayout.HORIZONTAL_CENTER, viagemLbl );

		layout.putConstraint( SpringLayout.SOUTH, painel, 5, SpringLayout.SOUTH, dataBt );
		
		return painel;
	}
	
	private JPanel setupPainelInfoPassageiros( ) {
		JPanel painel = new JPanel( new BorderLayout() );
		painel.setBorder( new TitledBorder("Passageiros") );
		painelPassageiros = new PainelListador();
		painel.add( painelPassageiros );
		return painel;
	}

	private JButton criarBotaoTextoClicavel( String txt, String tip, Font f ) {
		JButton txtBt = new JButton( txt );
		txtBt.setOpaque( false );
		txtBt.setBorderPainted( false );
		txtBt.setContentAreaFilled( false );
		txtBt.setFont( f );		
		txtBt.getModel().addChangeListener( c-> txtBt.setForeground( txtBt.getModel().isRollover()? Color.BLUE: Color.BLACK ) );
		txtBt.setToolTipText( tip );
		return txtBt;
	}
	
	private class PainelPassageiro extends JPanel {
		JButton nomeBt, lugarBt, malasBt;

		public PainelPassageiro(int num, String nome, String lugar, int numMalas ) {
			this.nomeBt = criarBotaoTextoClicavel(nome, "Alterar nome deste passageiro", mediaFontBold );
			this.lugarBt = criarBotaoTextoClicavel(lugar, "Reservar lugar", mediaFontBold );
			this.malasBt = criarBotaoTextoClicavel( numMalas+"", "Alterar número malas", mediaFontBold );

			nomeBt.addActionListener( a -> nomeBt.setText( mudarNomePassageiro( num ) ) );
			nomeBt.setHorizontalAlignment( JButton.LEFT );

			lugarBt.addActionListener( a -> { String lgr = reservarLugarPassageiro( num ); lugarBt.setText( lgr != null? lgr: "Sem lugar"); } );
			
			malasBt.addActionListener( a -> malasBt.setText( mudarBagagens(num) + "" ));
			
			SpringLayout layout = new SpringLayout();
			setLayout( layout );
			add( nomeBt );
			add( lugarBt );
			add( malasBt );
			
			layout.putConstraint( SpringLayout.NORTH, nomeBt, 1, SpringLayout.NORTH, this );
			layout.putConstraint( SpringLayout.WEST, nomeBt, 5, SpringLayout.WEST, this );
			layout.putConstraint( SpringLayout.EAST, nomeBt, 0, SpringLayout.HORIZONTAL_CENTER, this );

			layout.putConstraint( SpringLayout.BASELINE, lugarBt, 0, SpringLayout.BASELINE, nomeBt );
			layout.putConstraint( SpringLayout.WEST, lugarBt, 5, SpringLayout.EAST, nomeBt );
			layout.putConstraint( SpringLayout.EAST, lugarBt, 150, SpringLayout.WEST, lugarBt );

			layout.putConstraint( SpringLayout.BASELINE, malasBt, 0, SpringLayout.BASELINE, nomeBt );
			layout.putConstraint( SpringLayout.WEST, malasBt, 5, SpringLayout.EAST, lugarBt );
			
			layout.putConstraint( SpringLayout.SOUTH, this, 1, SpringLayout.SOUTH, lugarBt );
		}
	}
}


