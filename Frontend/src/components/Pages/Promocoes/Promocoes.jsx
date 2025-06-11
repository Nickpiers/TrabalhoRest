import { useNavigate } from "react-router-dom";
import { paths } from "../../../controller/paths";
import { Fundo } from "../../Dumb/Fundo";
import { Cabecalho } from "../../Dumb/Cabecalho";

export const Promocoes = () => {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Promocoes" />
        <button
          className="mb-30 mt-5"
          onClick={() => navigate(paths.inscreverPromocao)}
        >
          Inscrever na promoção
        </button>
        <button
          className="mb-30 mt-5"
          onClick={() => navigate(paths.cancelarPromocao)}
        >
          Cancelar promoção
        </button>
      </div>
      <Fundo />
    </div>
  );
};
