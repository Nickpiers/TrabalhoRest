import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Cabecalho } from "../Dumb/Cabecalho";
import { Input } from "../Dumb/Input";
import { consultarItinerario } from "../../controller/requestRest";
import { Fundo } from "../Dumb/Fundo";
import { CruzeiroCard } from "../Dumb/CruzeiroCard";
import { paths } from "../../controller/paths";

export const ConsultarItinerarios = () => {
  const navigate = useNavigate();

  const [destino, setDestino] = useState("");
  const [dataEmbarque, setDataEmbarque] = useState("");
  const [portoEmbarque, setPortoEmbarque] = useState("");

  const buscaItinerarios = async () => {
    await consultarItinerario({
      destino,
      dataEmbarque,
      portoEmbarque,
    });
  };

  const descricaoTeste = `Cruzeiro Mar Azul saindo de Santos em 01 de Agosto com duração de 7 noites.\nVisitando: Ilhabela, Florianópolis, Punta del Este. \nValor por pessoa: R$4200`;

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Consultar Itinerários" />
        <div className="flex flex-col gap-5">
          <Input value={destino} setValue={setDestino} descricao="Destino" />
          <Input
            value={dataEmbarque}
            setValue={setDataEmbarque}
            descricao="Data de Embarque"
          />
          <Input
            value={portoEmbarque}
            setValue={setPortoEmbarque}
            descricao="Porto de Embarque"
          />
        </div>
        <button className="mb-30 mt-5" onClick={buscaItinerarios}>
          Consultar
        </button>
        <div className="flex flex-col gap-5">
          <CruzeiroCard descricaoCruzeiro={descricaoTeste} id={1} />
          <CruzeiroCard descricaoCruzeiro={descricaoTeste} id={2} />
          <CruzeiroCard descricaoCruzeiro={descricaoTeste} id={3} />
        </div>
      </div>
      <Fundo />
    </div>
  );
};
