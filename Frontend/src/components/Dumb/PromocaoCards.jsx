export const PromocaoCards = ({ acaoBotao }) => {
  const descricao1 = "Reservas de 2 por 1 somente por 30 dias!";
  const descricao2 =
    "Reserve uma cabine e ganhe upgrade para varanda grátis! Promoção por tempo limitado.";
  const descricao3 =
    "Grupos com 4 ou mais pessoas ganham 20% de desconto! Válido até o fim do mês.";

  return (
    <div className="flex flex-col gap-5">
      <button
        className="text-lg px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
        onClick={() => acaoBotao(1)}
      >
        {descricao1}
      </button>
      <button
        className="text-lg px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
        onClick={() => acaoBotao(2)}
      >
        {descricao2}
      </button>
      <button
        className="text-lg px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
        onClick={() => acaoBotao(3)}
      >
        {descricao3}
      </button>
    </div>
  );
};
