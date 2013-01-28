Instr("SeasOfSubconsciousDrone", {
  arg freq = 440;
    /*gate = 1,*/
    /*amp = 1.0,*/
    /*attackTime = 2.0,
    releaseTime = 2.0;*/

  var out;
    /*envShape,
    env;

  envShape = Env.adsr(
    attackTime: attackTime,
    releaseTime: releaseTime,
    sustainLevel: 1.0,
    curve: [4, -4, -4, -4]
  );*/
  /*envShape.releaseNode = 2;*/

  /*envShape = Linen.kr(gate, 0.01, 0.7, 0.3, 2);*/

  /*envShape = Env.linen(
    attackTime: 2.0,
    sustainTime: sustain,
    releaseTime: 2.0,
    curve: \cub
  );*/

  /*env = EnvGen.ar(envShape, gate, doneAction: 2);*/
  /*env = Linen.kr(gate, 0.01, 0.7, 0.3, 2);*/
  
  out = Instr.ar("FreakyDroneMultiple", (
    /*baseFreq: 110,*/
    baseFreq: freq,
    baseModFreq: 80,
    numHarms: 7
  ));

  out = [out, AllpassC.ar(out, 0.07, 0.07)];

  /*out = env * (amp * out);*/

  /*out = Instr.ar("reverbs.reverberator3", (
    audio: out,
    revBalance: 0.6,
    revTime: 8
  ));*/

}, [
  \freq
  /*\gate,
  \amp*/
]);
