import { useNavigate } from "react-router-dom";
import { paths } from "../../controller/paths";

export const CruzeiroCard = ({ cruzeiro, veioDoConsultarItinerario }) => {
  const navigate = useNavigate();
  const {
    nomeNavio,
    portoEmbarque,
    datasDisponiveis,
    numeroNoites,
    lugaresVisitados,
    valorPorPessoa,
  } = cruzeiro;

  const montaDescricao = () => {
    return `Cruzeiro ${nomeNavio} saindo de ${portoEmbarque} em ${datasDisponiveis} com duração de ${numeroNoites} noites.\nVisitando: ${lugaresVisitados} \nValor por pessoa: R$${valorPorPessoa}`;
  };

  const aoClicar = () =>
    veioDoConsultarItinerario
      ? navigate(paths.marcarReserva, { state: { cruzeiro } })
      : () => {};

  return (
    <button
      className="text-lg px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
      onClick={aoClicar}
    >
      {montaDescricao()}
    </button>
  );
};
