import { Fundo } from "../../Dumb/Fundo";
import { Cabecalho } from "../../Dumb/Cabecalho";
import { PromocaoCards } from "../../Dumb/PromocaoCards";
import { inscreverPromocao } from "../../../controller/requestRest";

export const InscreverPromocao = () => {
  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Inscrever Promocao" />
        <PromocaoCards acaoBotao={inscreverPromocao} />
      </div>
      <Fundo />
    </div>
  );
};
