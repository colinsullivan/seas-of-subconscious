Instr("BassSlide", {

  arg duration = 2.0,
    gate = 0,
    freq = 220;

  var out,
    pbEnvShape,
    pbEnv;

  pbEnvShape = Env.perc(
    attackTime: 0.01,
    releaseTime: duration,
    curve: 3
  );
  pbEnv = EnvGen.kr(pbEnvShape, gate, doneAction: 2).range(0.5, 1.0);

  out = Instr.ar("cs.fm.WideBass", (
    freq: freq,
    pb: pbEnv
  ));

  out;
}, [
  ObjectSpec(2.0),
  \gate,
  \freq
]);
