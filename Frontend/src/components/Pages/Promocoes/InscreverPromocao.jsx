import { Fundo } from "../../Dumb/Fundo";
import { Cabecalho } from "../../Dumb/Cabecalho";
import { PromocaoCards } from "../../Dumb/PromocaoCards";
import { inscreverPromocao } from "../../../controller/requestRest";
import { usePromocaoSSE } from "../../PromocaoSSEContext";

export const InscreverPromocao = () => {
  const { startSSE } = usePromocaoSSE();
  const clientId = sessionStorage.getItem("clientId");

  const handleInscricao = async (idPromocao) => {
    if (!clientId) {
      alert("ClientId não encontrado na sessão.");
      return;
    }

    startSSE(clientId, idPromocao);

    try {
      const resposta = await inscreverPromocao(idPromocao);
      alert(resposta.mensagem);
    } catch (err) {}
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Inscrever Promocao" />
        <PromocaoCards acaoBotao={handleInscricao} />
      </div>
      <Fundo />
    </div>
  );
};
