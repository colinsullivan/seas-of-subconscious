(
  var m, mBounds;
 
  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.outDevice = "Soundflower (64ch)";
  s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  /*s.options.hardwareBufferSize = 128;*/
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);
  
  s.doWhenBooted({
    ({
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

        out;

      }, [
        \freq,
        \freq,
        [1, 100]
      ]);

      Instr("LPFer", {
        arg in, cutoffFreq = 100;

        var out;

        out = LPF.ar(in, cutoffFreq);

      });

      Instr("LPFerModulated", {

        arg in, cutoffMinFreq, cutoffMaxFreq, cutoffModFreq;

        var out;

        out = Instr.ar("LPFer", (
          in: in,
          cutoffFreq: SinOsc.ar(cutoffModFreq).range(cutoffMinFreq, cutoffMaxFreq)
        ));

      });


      Instr("SeasOfSubconciousDrone", {
        var out;
        
        out = Instr.ar("FreakyDroneMultiple", (
          baseFreq: 110,
          baseModFreq: 80,
          numHarms: 7
        ));

        out = Instr.ar("LPFerModulated", (
          in: out,
          cutoffMinFreq: 100,
          cutoffMaxFreq: 125,
          cutoffModFreq: 0.25
        ));

        /*out = GVerb.ar(out,
          roomsize: 3,
          revtime: 1.0,
          damping: 0.6,
          inputbw: 0.5,
          spread: 15
        );*/

      });

      Patch("SeasOfSubconciousDrone");
      
    }.play();)
  
  });

)



