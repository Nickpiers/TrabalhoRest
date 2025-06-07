export const consultarItinerario = async () => {
  console.warn(await requestBack("/reservas/itinerarios"));
};

export const criarReserva = async () => {
  console.warn(await requestBack("/reservas/criarReserva", "cria reserva"));
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
