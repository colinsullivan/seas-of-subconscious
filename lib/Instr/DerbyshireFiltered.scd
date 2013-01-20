Instr("DerbyshireFiltered", {


  arg buf,
    gate,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    convertToStereo;

  var out;

  out = Instr.ar("cs.sfx.PlayBuf", (
    buf: buf,
    gate: gate,
    playbackRate: playbackRate,
    startTime: startTime,
    attackTime: attackTime,
    releaseTime: releaseTime,
    convertToStereo: convertToStereo
  ));

  out = Instr.ar("cs.fx.LPFer", (

    in: out,
    cutoffFreq: 400
  
  ));

}, [
  \buffer,
  \gate,
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  ObjectSpec(1.0),
  ObjectSpec(1.0)
]);
