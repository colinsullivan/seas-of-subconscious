Instr("FreakyDroneMultiple", {
  arg baseFreq,
    baseModFreq,
    numHarms;

  var out = Silence.ar(),
    harmNum,
    freq,
    modFreq;

  (numHarms.asInteger()).do({
    arg i;
    
    harmNum = i + 2;
    freq = baseFreq * harmNum + Rand(0.0, 10.0);
    modFreq = baseModFreq * harmNum + Rand(0.0, 10.0);

    out = out + Instr.ar("FreakyDrone", (
      baseFreq: freq,
      baseModFreq: modFreq
    ));
  
  });

  out;

}, [
  \freq,
  \freq,
  [1, 100]
]);
