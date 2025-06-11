export const consultarItinerario = async (body) => {
  console.warn(await requestBack("/reservas/itinerarios", body));
};

export const criarReserva = async (params) => {
  const body = {
    nomeCompleto: "nicolas",
    dataEmbarque: "10",
    numeroPassageiros: 2,
    numeroCabines: 1,
    idCruzeiro: 1,
  };
  escutarLink(body.nomeCompleto);
  console.warn(await requestBack("/reservas/criarReserva", body));
};

export const escutarLink = (nomeCompleto) => {
  const eventSource = new EventSource(
    `http://localhost:8080/reserva/stream/${nomeCompleto}`
  );

  eventSource.onmessage = (event) => {
    console.log("🎉 Link recebido:", event.data);
    alert("Link para pagamento: " + event.data);
    eventSource.close();
  };

  eventSource.onerror = (err) => {
    console.error("Erro SSE:", err);
    alert("Erro ao escutar link de pagamento.");
    eventSource.close();
  };
};

export const cancelarReserva = async () => {
  console.warn(
    await requestBack("/reservas/cancelarReserva", "cancela reserva")
  );
};

export const inscreverPromocao = async () => {
  console.warn(
    await requestBack("/reservas/inscreverPromocao", "inscreve varanda")
  );
};

export const cancelarPromocao = async () => {
  console.warn(
    await requestBack("/reservas/cancelarPromocao", "cancela varanda")
  );
};

export const requestBack = async (uri, body) => {
  try {
    const options = {
      method: body ? "POST" : "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    if (body) {
      options.body = JSON.stringify(body);
    }

    const response = await fetch(`http://localhost:8080${uri}`, options);

    if (!response.ok) {
      setMensagem("Erro na conexão com o backend");
      throw new Error(`Erro HTTP: ${response.status}`);
    }

    const data = await response.text();
    return data;
  } catch (err) {
    setMensagem("Erro na conexão com o backend");
    console.error(err);
  }
};
