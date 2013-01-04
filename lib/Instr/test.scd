Instr("test", {
  arg gate = 1, freq = 440;

  Linen.kr(gate, 0.01, 0.7, 0.3, 2) * SinOsc.ar(freq);
}, [
  \gate,
  \freq
]);
