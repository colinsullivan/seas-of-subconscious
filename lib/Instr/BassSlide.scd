Instr("BassSlide", {

  arg duration = 2.0,
    gate = 0,
    freq = 110,
    amp = 0.9;

  var out,
    pbEnvShape,
    pbEnv;

  pbEnvShape = Env.perc(
    attackTime: 0.00,
    releaseTime: duration,
    curve: 3
  );
  pbEnv = EnvGen.kr(pbEnvShape, gate).range(0.25, 1.0);

  out = Instr.ar("cs.fm.WideBass", (
    freq: freq,
    pb: pbEnv,
    gate: gate,
    toneModulatorGainMultiplier: 0.3,
    amp: amp
  ));

  out;
}, [
  \beats,
  \gate,
  \freq,
  \amp
]);
