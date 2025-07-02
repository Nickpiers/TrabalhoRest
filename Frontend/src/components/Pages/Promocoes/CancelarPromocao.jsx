import { Fundo } from "../../Dumb/Fundo";
import { Cabecalho } from "../../Dumb/Cabecalho";
import { PromocaoCards } from "../../Dumb/PromocaoCards";
import { cancelarPromocao } from "../../../controller/requestRest";

export const CancelarPromocao = () => {
  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Cancelar Promocao" />
        <PromocaoCards acaoBotao={cancelarPromocao} />
      </div>
      <Fundo />
    </div>
  );
};
