import { useNavigate } from "react-router-dom";
import { Cabecalho } from "../../Dumb/Cabecalho";
import { Fundo } from "../../Dumb/Fundo";
import { paths } from "../../../controller/paths";

export const Reservas = () => {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Reservas" />
        <button
          className="mb-30 mt-5"
          onClick={() => navigate(paths.consultarItinerarios)}
        >
          Criar nova reserva
        </button>
        <button
          className="mb-30 mt-5"
          onClick={() => navigate(paths.cancelarReserva)}
        >
          Cancelar reserva
        </button>
      </div>
      <Fundo />
    </div>
  );
};
