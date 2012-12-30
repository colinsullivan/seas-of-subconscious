s.boot;
s.meter;

Instr("FreakyDrone", {

  arg baseFreq, baseModFreq;

  var carrier,
    modulator,
    modulatorModulator,
    out,
    modFreq,
    freq;
  
  modulatorModulator = SinOsc.ar(Rand(0.05, 0.2));
  modFreq = baseModFreq + modulatorModulator.range(0, 0.33 * baseModFreq);
  
  modulator = LFTri.ar(modFreq);
  freq = baseFreq + modulator.range(0, 1.33 * baseFreq);

  carrier = SinOsc.ar(freq);

  out = [carrier, carrier];
}, [
  \freq,
  \freq
]);

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

  out = LPF.ar(out, 100);

  out;

}, [
  \freq,
  \freq,
  [1, 100]
]);

Instr("LPFer",

})


({

  Patch("FreakyDroneMultiple", (
    baseFreq: 110,
    baseModFreq: 80,
    numHarms: 7
  ));
  
}.play();)
